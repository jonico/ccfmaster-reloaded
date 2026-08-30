package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
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
public class PropertiesDirectionConfigPersisterFactoryTest {
    @Autowired
    private DirectionDataOnDemand dod;

    @Test
    public void checkFileName() {
        final Direction direction = dod.getNewTransientDirection(42);
        direction.setDirection(Directions.FORWARD);
        final Landscape landscape = direction.getLandscape();
        final File baseDir = new File(System.getProperty("java.io.tmpdir"));
        PropertiesDirectionConfigPersisterFactory factory = new PropertiesDirectionConfigPersisterFactory(
                baseDir);

        final PropertiesConfigItemPersister<DirectionConfig> persister = (PropertiesConfigItemPersister<DirectionConfig>) factory
                .get(direction);
        final File propFile = persister.getPropFile();
        final File propDir = new File(baseDir, "landscape" + landscape.getId());
        final String fileName = landscape.getTeamForge().getSystemKind() + "2"
                + landscape.getParticipant().getSystemKind() + ".properties";
        assertEquals(new File(propDir, fileName), propFile);
        assertTrue(propDir.exists(), "directory wasn't created");
        assertTrue(propDir.delete(), "directory contains data");
    }
}
