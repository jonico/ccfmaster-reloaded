package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.MockFieldMappingExternalAppTemplatePersisterFactory;

@RooIntegrationTest(entity = FieldMappingExternalAppTemplate.class)
public class FieldMappingExternalAppTemplateIntegrationTest {
    @Autowired
    FieldMappingExternalAppTemplateDataOnDemand fmeatdod;

    @Test
    public void savesOnPersistAndMerge() {
        FieldMappingExternalAppTemplate fmeat = fmeatdod
                .getNewTransientFieldMappingExternalAppTemplate(42);
        MockFieldMappingExternalAppTemplatePersisterFactory mockFmeatpf = new MockFieldMappingExternalAppTemplatePersisterFactory();
        fmeat.setPersisterFactory(mockFmeatpf);
        fmeat.persist();
        assertTrue(mockFmeatpf.calledSave, "save wasn't called on persist().");

        mockFmeatpf = new MockFieldMappingExternalAppTemplatePersisterFactory();
        fmeat = FieldMappingExternalAppTemplate
                .findFieldMappingExternalAppTemplate(fmeat.getId());
        assertNotNull(fmeat, "couldn't find fieldMappingExternalAppTemplate after persist.");
        fmeat.setPersisterFactory(mockFmeatpf);
        fmeat = fmeat.merge();
        assertTrue(mockFmeatpf.calledSave, "save wasn't called on merge().");

    }

    @Test
    public void testMarkerMethod() {
    }
}
