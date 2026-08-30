package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.Mapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
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
public class StorageDirectoryTest {
    @Autowired
    private FieldMappingDataOnDemand                    fmDod;
    @Autowired
    private FieldMappingExternalAppTemplateDataOnDemand fmeatDod;
    @Autowired
    private FieldMappingLandscapeTemplateDataOnDemand   fmltDod;

    private File                                        baseDir = null;

    @BeforeEach
    public void createTempDir() throws IOException {
        baseDir = File.createTempFile(StorageDirectoryTest.class.getName(),
                "dir");
        baseDir.delete();
        baseDir.mkdir();
    }

    @AfterEach
    public void removeTempDir() throws IOException {
        FileUtils.forceDelete(baseDir);
    }

    @Test
    public void testFieldMappingDirectory() {
        final FieldMapping fm = fmDod.getNewTransientFieldMapping(0);
        final RepositoryMappingDirection rmd = fm.getParent();
        final File dir = mappingStorageDirectoryTests(fm);
        assertTrue(dir.toString().contains(rmd.getDirection().toString()), "RMD direction not in directory");
    }

    @Test
    public void testFieldMappingExternalAppTemplateDirectory() {
        final FieldMappingExternalAppTemplate fmeat = fmeatDod
                .getNewTransientFieldMappingExternalAppTemplate(0);
        final File dir = mappingStorageDirectoryTests(fmeat);
        assertTrue(dir.toString()
                        .toLowerCase()
                        .contains(fmeat.getDirection().toString().toLowerCase()), "template direction not in directory " + dir);
        assertTrue(dir.toString()
                .toLowerCase().matches(".*?prpl\\d+.*"), "linkId not in directory " + dir);
    }

    @Test
    public void testFieldMappingLandscapeTemplateDirectory() {
        final FieldMappingLandscapeTemplate fmeat = fmltDod
                .getNewTransientFieldMappingLandscapeTemplate(0);
        final File dir = mappingStorageDirectoryTests(fmeat);
        assertTrue(dir.toString()
                        .toLowerCase()
                        .contains(fmeat.getDirection().toString().toLowerCase()), "template direction not in directory " + dir);
    }

    private File mappingStorageDirectoryTests(Mapping<?> mapping) {
        File dir = mapping.getStorageDirectory(baseDir);
        System.out.println(dir);
        //		assertTrue(dir + " doesn't exist " + dir, dir.exists());
        //		assertTrue(dir + " is not a directory " + dir, dir.isDirectory());
        assertTrue(dir.toString()
                .toLowerCase().matches(".*?landscape\\d+.*"), "landscape not in directory path " + dir);
        return dir;
    }
}
