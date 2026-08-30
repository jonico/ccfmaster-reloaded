package com.collabnet.ccf.ccfmaster.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.ExecutedCommand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration
/*
 * Was: extends AbstractTransactionalJUnit4SpringContextTests. That class is JUnit 4 only
 * (it is annotated @RunWith(SpringJUnit4ClassRunner.class) via its superclass) and
 * deprecated for removal in Spring 6. Its whole contribution here was loading the context
 * and wrapping each test in a rolled-back transaction - none of the 14 subclasses touched
 * its jdbcTemplate, applicationContext or logger members - so @ExtendWith(SpringExtension)
 * plus @Transactional is an exact replacement.
 */
@ExtendWith(SpringExtension.class)
@Transactional
public class CcfCoreStatusSystemTest {

    @Autowired
    @Qualifier("ccfHome")
    private File          ccfHome;
    @Autowired
    @Value("${ccf.db.port}")
    String                dbPort;
    protected Participant tf;

    protected Participant swp;
    protected Landscape   swptf;

    protected Direction   tf2swp;
    private CcfCoreStatus coreStatus;

    @AfterEach
    public void cleanup() throws InterruptedException, IOException {
        coreStatus.setExecutedCommand(ExecutedCommand.STOP);
        coreStatus.merge();
        TimeUnit.SECONDS.sleep(11);
        FileUtils.forceDelete(new File(ccfHome, "landscape" + swptf.getId()));
    }

    @BeforeEach
    public void setup() {
        tf = new Participant();
        tf.setDescription("Description for TF landscape");
        tf.setSystemId("TeamForge");
        tf.setSystemKind(SystemKind.TF);
        tf.setTimezone(Timezone.Europe_SLASH_Belgrade);
        tf.setEncoding("normal");
        tf.persist();

        swp = new Participant();
        swp.setDescription("SWP");
        swp.setSystemId("SWP");
        swp.setSystemKind(SystemKind.SWP);
        swp.setTimezone(Timezone.Europe_SLASH_Paris);
        swp.persist();

        swptf = new Landscape();
        swptf.setTeamForge(tf);
        swptf.setParticipant(swp);
        swptf.setName("SWPTF");
        swptf.setPlugId("plug1234");
        swptf.persist();

        tf2swp = new Direction();
        tf2swp.setDescription("TF-SWP");
        tf2swp.setLandscape(swptf);
        tf2swp.setDirection(Directions.FORWARD);
        tf2swp.persist();

        directionConfig(tf2swp, "ccf.direction.logmessagetemplate", "template");
        landscapeConfig(swptf, "ccf.landscape.tf.username", "admin");
        landscapeConfig(swptf, "ccf.landscape.tf.password", "admin");
        participantConfig(tf, "ccf.participant.tf.url", "http://pebblevm");
        directionConfig(tf2swp, "ccf.direction.tf.max.attachmentsize", "1234");
        landscapeConfig(swptf, "ccf.landscape.swp.username", "user");
        landscapeConfig(swptf, "ccf.landscape.swp.password", "pass");
        landscapeConfig(swptf, "ccf.landscape.swp.resync.username",
                "resyncuser");
        landscapeConfig(swptf, "ccf.landscape.swp.resync.password",
                "resyncpass");
        participantConfig(swp, "ccf.participant.swp.url", "invalid");
        this.coreStatus = CcfCoreStatus.findCcfCoreStatusesByDirection(tf2swp)
                .getSingleResult();
    }

    @Test
    public void startAndStopCore() throws InterruptedException {
        coreStatus.setExecutedCommand(ExecutedCommand.START);
        coreStatus.merge();
        assertEquals(CoreState.STARTING, coreStatus.getCurrentStatus());
        TimeUnit.SECONDS.sleep(11);
        assertEquals(CoreState.STARTED, coreStatus.getCurrentStatus());
        coreStatus.setExecutedCommand(ExecutedCommand.STOP);
        coreStatus.merge();
        TimeUnit.SECONDS.sleep(50);
        assertEquals(CoreState.STOPPED, coreStatus.getCurrentStatus());
    }

    private static DirectionConfig directionConfig(Direction parent,
            String name, String val) {
        assertTrue(name.startsWith("ccf.direction."));
        DirectionConfig res = new DirectionConfig();
        res.setDirection(parent);
        res.setName(name);
        res.setVal(val);
        res.persist();
        return res;
    }

    private static LandscapeConfig landscapeConfig(Landscape parent,
            String name, String val) {
        assertTrue(name.startsWith("ccf.landscape."));
        LandscapeConfig res = new LandscapeConfig();
        res.setLandscape(parent);
        res.setName(name);
        res.setVal(val);
        res.persist();
        return res;
    }

    private static ParticipantConfig participantConfig(Participant parent,
            String name, String val) {
        assertTrue(name.startsWith("ccf.participant."));
        ParticipantConfig res = new ParticipantConfig();
        res.setParticipant(parent);
        res.setName(name);
        res.setVal(val);
        res.persist();
        return res;
    }
}
