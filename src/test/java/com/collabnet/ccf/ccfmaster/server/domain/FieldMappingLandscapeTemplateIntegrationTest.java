package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.MockFieldMappingLandscapeTemplatePersisterFactory;

@RooIntegrationTest(entity = FieldMappingLandscapeTemplate.class)
public class FieldMappingLandscapeTemplateIntegrationTest {

    @Autowired
    FieldMappingLandscapeTemplateDataOnDemand fmltdod;

    @Test
    public void savesOnPersistAndMerge() {
        FieldMappingLandscapeTemplate fmlt = fmltdod
                .getNewTransientFieldMappingLandscapeTemplate(42);
        MockFieldMappingLandscapeTemplatePersisterFactory mockFmltpf = new MockFieldMappingLandscapeTemplatePersisterFactory();
        fmlt.setPersisterFactory(mockFmltpf);
        fmlt.persist();
        assertTrue(mockFmltpf.calledSave, "save wasn't called on persist().");

        mockFmltpf = new MockFieldMappingLandscapeTemplatePersisterFactory();
        fmlt = FieldMappingLandscapeTemplate
                .findFieldMappingLandscapeTemplate(fmlt.getId());
        assertNotNull(fmlt, "couldn't find fieldMappingLandscapeTemplate after persist.");
        fmlt.setPersisterFactory(mockFmltpf);
        fmlt = fmlt.merge();
        assertTrue(mockFmltpf.calledSave, "save wasn't called on merge().");

    }

    @Test
    public void testMarkerMethod() {
    }

    @Test
    public void throwsWhenBadName() {
        org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class, () -> {    
            FieldMappingLandscapeTemplate fmlt = fmltdod
                    .getNewTransientFieldMappingLandscapeTemplate(42);
            fmlt.setName("$!@invalid");
            fmlt.persist();
                });
    }
}
