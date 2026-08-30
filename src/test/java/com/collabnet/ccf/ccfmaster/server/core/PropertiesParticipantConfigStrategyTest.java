package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.core.PropertiesConfigItemPersister;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigDataOnDemand;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration()
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
public class PropertiesParticipantConfigStrategyTest {

    @Autowired
    private ParticipantConfigDataOnDemand dod;
    private ParticipantConfig             pc;

    @Test
    public void deleteProperties() throws IOException {
        final PropertiesConfigItemPersister<?> strategy = (PropertiesConfigItemPersister<?>) pc
                .getPersister();
        pc.remove();
        final File propFile = strategy.getPropFile();
        try {
            assertTrue(propFile.exists(), propFile + " doesn't exist.");
            Properties props = strategy.loadProperties(propFile);
            assertFalse(props.containsKey(pc.getName()), "properties have key " + pc.getName());
            assertNull(props.getProperty(pc.getName()), "value " + pc.getVal() + " in properties.");
        } finally {
            propFile.delete();
        }
    }

    @BeforeEach
    public void init() {
        this.pc = dod.getRandomParticipantConfig();
    }

    @Test
    public void mergeTriggersSaveProperties() throws IOException {
        final PropertiesConfigItemPersister<?> strategy = (PropertiesConfigItemPersister<?>) pc
                .getPersister();
        final String newVal = "changed";
        pc.setVal(newVal);
        pc.merge();
        final File propFile = strategy.getPropFile();
        try {
            assertTrue(propFile.exists(), propFile + " doesn't exist.");
            Properties props = strategy.loadProperties(propFile);
            assertTrue(props.containsKey(pc.getName()), "properties don't have key " + pc.getName());
            assertEquals(newVal, props.getProperty(pc.getName()), "value " + pc.getVal() + " not in properties.");
        } finally {
            propFile.delete();
        }
    }

    @Test
    public void persistTriggersSaveProperties() throws IOException {
        final PropertiesConfigItemPersister<?> strategy = (PropertiesConfigItemPersister<?>) pc
                .getPersister();
        pc.persist();
        final File propFile = strategy.getPropFile();
        try {
            assertTrue(propFile.exists(), propFile + " doesn't exist.");
            Properties props = strategy.loadProperties(propFile);
            assertTrue(props.containsKey(pc.getName()), "properties don't have key " + pc.getName());
            assertEquals(pc.getVal(), props.getProperty(pc.getName()), "value " + pc.getVal() + " not in properties.");
        } finally {
            propFile.delete();
        }
    }

    @Test
    public void testDeleteWithoutDB() throws IOException {
        final File propFile = File.createTempFile("test", ".properties");
        propFile.deleteOnExit();
        final PropertiesConfigItemPersister<ParticipantConfig> strategy = new PropertiesConfigItemPersister<ParticipantConfig>(
                propFile);
        strategy.save(pc);
        strategy.delete(pc);
        assertTrue(propFile.exists(), propFile + " doesn't exist.");
        Properties props = strategy.loadProperties(propFile);
        assertFalse(props.containsKey(pc.getName()), "properties have key " + pc.getName());
        assertNull(props.getProperty(pc.getName()), "value " + pc.getVal() + " in properties.");
    }

    @Test
    public void testSaveWithoutDB() throws IOException {
        final File propFile = File.createTempFile("test", ".properties");
        propFile.deleteOnExit();
        final PropertiesConfigItemPersister<ParticipantConfig> strategy = new PropertiesConfigItemPersister<ParticipantConfig>(
                propFile);
        strategy.save(pc);
        assertTrue(propFile.exists(), propFile + " doesn't exist.");
        Properties props = strategy.loadProperties(propFile);
        assertTrue(props.containsKey(pc.getName()), "properties don't have key " + pc.getName());
        assertEquals(pc.getVal(), props.getProperty(pc.getName()), "value " + pc.getVal() + " not in properties.");
    }

}
