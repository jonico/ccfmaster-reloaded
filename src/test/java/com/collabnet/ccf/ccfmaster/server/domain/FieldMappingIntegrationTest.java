package com.collabnet.ccf.ccfmaster.server.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.MockFieldMappingPersisterFactory;

@RooIntegrationTest(entity = FieldMapping.class)
public class FieldMappingIntegrationTest {

    @Autowired
    FieldMappingDataOnDemand fmdod;

    @Test
    public void doesNotPersistWhenScopeIsNotRMD() {
        MockFieldMappingPersisterFactory mockFmpf = new MockFieldMappingPersisterFactory();
        FieldMapping fm = fmdod.getNewTransientFieldMapping(42);
        fm.setPersisterFactory(mockFmpf);
        fm.setScope(FieldMappingScope.CCF_CORE);
        assertFalse(fm.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
        fm.persist();
        assertFalse(mockFmpf.calledSave, "save was called on persist.");

        mockFmpf = new MockFieldMappingPersisterFactory();
        fm = FieldMapping.findFieldMapping(fm.getId());
        assertNotNull(fm, "couldn't find fieldMapping after persist.");
        fm.setPersisterFactory(mockFmpf);
        fm = fm.merge();
        assertFalse(mockFmpf.calledSave, "save was called on merge().");
    }

    @Test
    public void paramMustBeAlphaNumerical() {
        org.junit.jupiter.api.Assertions.assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {    
            FieldMapping fm = fmdod.getNewTransientFieldMapping(23);
            fm.setName("illegalParam.xsl");
            fm.persist();
                });
    }

    @Test
    public void persistsWhenScopeIsRMD() {
        MockFieldMappingPersisterFactory mockFmpf = new MockFieldMappingPersisterFactory();
        FieldMapping fm = fmdod.getNewTransientFieldMapping(42);
        fm.setPersisterFactory(mockFmpf);
        assertTrue(fm.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
        fm.persist();
        assertTrue(mockFmpf.calledSave, "save wasn't called on persist().");

        mockFmpf = new MockFieldMappingPersisterFactory();
        fm = FieldMapping.findFieldMapping(fm.getId());
        assertNotNull(fm, "couldn't find fieldMapping after persist.");
        fm.setPersisterFactory(mockFmpf);
        fm = fm.merge();
        assertTrue(mockFmpf.calledSave, "save wasn't called on merge().");
    }

    @Test
    public void testMarkerMethod() {
    }
}
