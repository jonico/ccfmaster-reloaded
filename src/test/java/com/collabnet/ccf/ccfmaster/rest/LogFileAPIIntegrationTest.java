package com.collabnet.ccf.ccfmaster.rest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.LogFileList;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.google.common.io.Files;

public class LogFileAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private DirectionDataOnDemand dod;

    private String                ccfHome = "/tmp";

    @AfterEach
    public void clear() {
        String deleteDirString = String.format("%s%d", "landscape", dod
                .getSpecificDirection(0).getLandscape().getId());
        File deleteDir = new File(ccfHome, deleteDirString);
        FileUtils.deleteQuietly(deleteDir);
    }

    @BeforeEach
    public void setUp() {
        String logDirString = String.format("%s%d%s", "landscape", dod
                .getSpecificDirection(0).getLandscape().getId(),
                "/samples/TFSWP/TF2SWP/logs");
        File logDir = new File(ccfHome, logDirString);
        logDir.mkdirs();
        try {
            Files.touch(new File(logDir, "sample-ccf-info.log"));
            FileUtils.write(new File(logDir, "sample-ccf-info.log"),
                    "Testing\n logFile\n Let see who testing goes\n");
        } catch (IOException e) {
        }
    }

    @Test
    public void testLogFileListFind() throws IOException {
        Participant tf = new Participant();
        tf.setSystemKind(SystemKind.TF);
        Participant swp = new Participant();
        swp.setSystemKind(SystemKind.SWP);
        Direction obj = dod.getSpecificDirection(0);
        if (!(obj.getLandscape().getTeamForge().getSystemKind() == SystemKind.TF)) {
            obj.getLandscape().getTeamForge().setSystemKind(SystemKind.TF);
            obj.getLandscape().getParticipant().setSystemKind(SystemKind.SWP);
        }
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
        LogFileList logObj = restTemplate.getForObject(ccfAPIUrl
                + "/directions/" + id + "/logs/", LogFileList.class);
        org.junit.jupiter.api.Assertions.assertNotNull(logObj, "Find method for 'Direction' illegally returned null for id '"
                        + id + "'");
    }

    /*
     * Below method is commented because rest template expects header values to
     * be set Since header is missing below test case throws exception stating
     * content-type is not set.
     * @Test public void testLogFile() throws IOException, URISyntaxException {
     * Direction obj = dod.getSpecificDirection(1);
     * org.junit.jupiter.api.Assertions.assertNotNull
     * ("Data on demand for 'Direction' failed to initialize correctly", obj);
     * java.lang.Long id = obj.getId(); org.junit.jupiter.api.Assertions.assertNotNull(
     * "Data on demand for 'Direction' failed to provide an identifier", id);
     * String value = restTemplate.getForObject(ccfAPIUrl + "/directions/"+ id
     * +"/logs/sample-ccf-info.log?firstLine=1&maxlines=2",String.class); }
     */

}
