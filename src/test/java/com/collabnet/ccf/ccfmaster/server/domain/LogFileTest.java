package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.io.Files;
import com.google.common.io.Resources;

public class LogFileTest {

    private static final String LOG_FILE       = "persistence.xml";

    private static final String EMPTY_LOG_FILE = "empty.log";

    private File                ccfHome;
    private File                logDir;

    /* a sample landscape for us to play around with. not persisted */
    private Participant         tf;
    private Participant         swp;
    private Landscape           landscape;
    private Direction           direction;

    @AfterEach
    public void cleanup() throws IOException {
        FileUtils.deleteQuietly(ccfHome);
    }

    @Test
    public void directoryTraversalThrows() throws IOException {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {    
            new LogFile(ccfHome, direction, "../logs");
                });
    }

    @Test
    public void fileNotFoundThrows() throws IOException {
        org.junit.jupiter.api.Assertions.assertThrows(FileNotFoundException.class, () -> {    
            new LogFile(ccfHome, direction, "doesNotExist.txt");
                });
    }

    @Test
    public void nullFileNameThrows() throws IOException {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {    
            new LogFile(ccfHome, direction, null);
                });
    }

    @BeforeEach
    public void setup() throws IOException {
        tf = new Participant();
        tf.setSystemKind(SystemKind.TF);
        swp = new Participant();
        swp.setSystemKind(SystemKind.SWP);
        landscape = new Landscape();
        landscape.setId(1L);
        landscape.setTeamForge(tf);
        landscape.setParticipant(swp);
        direction = new Direction();
        direction.setDirection(Directions.FORWARD);
        direction.setLandscape(landscape);

        ccfHome = Files.createTempDir();
        logDir = new File(ccfHome, "landscape1/samples/TFSWP/TF2SWP/logs");
        logDir.mkdirs();
        Files.touch(new File(logDir, EMPTY_LOG_FILE));
        final FileOutputStream to = new FileOutputStream(new File(logDir,
                LOG_FILE));
        Resources.copy(Resources.getResource("META-INF/" + LOG_FILE), to);
        to.close();
    }

    @Test
    public void validEmptyLogFile() throws IOException {
        LogFile logFile = new LogFile(ccfHome, direction, EMPTY_LOG_FILE);
        assertEquals(EMPTY_LOG_FILE, logFile.getName());
        assertSame(direction, logFile.getDirection());
        assertEquals(0L, logFile.getSize());
        LineIterator it = logFile.lines();
        try {
            assertFalse(it.hasNext());
            it.nextLine();
            fail("NoSuchElementException wasn't thrown at nextLine()");
        } catch (NoSuchElementException expected) {
        } finally {
            it.close();
        }
    }

    @Test
    public void validLogFile() throws IOException {
        LogFile logFile = new LogFile(ccfHome, direction, LOG_FILE);
        assertEquals(LOG_FILE, logFile.getName());
        assertSame(direction, logFile.getDirection());
        assertFalse(0L == logFile.getSize(), "file " + LOG_FILE + " was empty");
        LineIterator it = logFile.lines();
        try {
            assertTrue(it.hasNext());
            it.nextLine();
        } catch (NoSuchElementException expected) {
        } finally {
            it.close();
        }
    }
}
