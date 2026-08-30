package com.collabnet.ccf.ccfmaster.server.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = CcfCoreStatus.class)
public class CcfCoreStatusIntegrationTest {

    @Autowired
    private CcfCoreStatusDataOnDemand dod;
    @Autowired
    private DirectionDataOnDemand     directionDataOnDemand;

    @Test
    public void nonPersistedDirectionHasNoCoreStatus() {
        Direction dir = directionDataOnDemand
                .getNewTransientDirection(Integer.MAX_VALUE);
        Assertions.assertNull(dir.getId());
        Assertions.assertNull(CcfCoreStatus.findCcfCoreStatus(dir.getId()));
        Assertions.assertTrue(CcfCoreStatus.findCcfCoreStatusesByDirection(dir)
                .getResultList().isEmpty());
    }

    @Test
    public void testCountCcfCoreStatuses() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomCcfCoreStatus(), "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .countCcfCoreStatuses();
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'CcfCoreStatus' incorrectly reported there were no entries");
    }

    @Test
    public void testFindAllCcfCoreStatuses() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomCcfCoreStatus(), "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .countCcfCoreStatuses();
        org.junit.jupiter.api.Assertions
                .assertTrue(count < 250, "Too expensive to perform a find all test for 'CcfCoreStatus', as there are "
                                + count
                                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test");
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus> result = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .findAllCcfCoreStatuses();
        org.junit.jupiter.api.Assertions.assertNotNull(result, "Find all method for 'CcfCoreStatus' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertTrue(result.size() > 0, "Find all method for 'CcfCoreStatus' failed to return any data");
    }

    @Test
    public void testFindCcfCoreStatus() {
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod
                .getRandomCcfCoreStatus();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'CcfCoreStatus' failed to provide an identifier");
        obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .findCcfCoreStatus(id);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'CcfCoreStatus' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'CcfCoreStatus' returned the incorrect identifier");
    }

    @Test
    public void testFindCcfCoreStatusEntries() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomCcfCoreStatus(), "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .countCcfCoreStatuses();
        if (count > 20)
            count = 20;
        java.util.List<com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus> result = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .findCcfCoreStatusEntries(0, (int) count);
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'CcfCoreStatus' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'CcfCoreStatus' returned an incorrect number of entries");
    }

    @Test
    public void testFlush() {
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod
                    .getRandomCcfCoreStatus();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'CcfCoreStatus' failed to provide an identifier");
            obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                    .findCcfCoreStatus(id);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'CcfCoreStatus' illegally returned null for id '"
                            + id + "'");
            boolean modified = dod.modifyCcfCoreStatus(obj);
            java.lang.Integer currentVersion = obj.getVersion();
            obj.flush();
            org.junit.jupiter.api.Assertions
                    .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                    || !modified, "Version for 'CcfCoreStatus' failed to increment on flush directive");
                });
    }

    @Test
    public void testMarkerMethod() {
    }

    @Test
    public void testMerge() {
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod
                .getRandomCcfCoreStatus();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'CcfCoreStatus' failed to provide an identifier");
        obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                .findCcfCoreStatus(id);
        boolean modified = dod.modifyCcfCoreStatus(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus merged = (com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus) obj
                .merge();
        //        obj.flush();
        org.junit.jupiter.api.Assertions
                .assertEquals(merged.getId(), id, "Identifier of merged object not the same as identifier of original object");
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'CcfCoreStatus' failed to increment on merge and flush directive");
    }

    @Test
    public void testPersist() {
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class, () -> {    
            org.junit.jupiter.api.Assertions
                    .assertNotNull(dod.getRandomCcfCoreStatus(), "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
            Direction dir = directionDataOnDemand.getRandomDirection();
            com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = CcfCoreStatus
                    .findCcfCoreStatus(dir.getId());
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'CcfCoreStatus' failed to provide a new transient entity");
            obj.setId(null);
            obj.setVersion(null);
            org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'CcfCoreStatus' identifier to be null");
            obj.persist();
            obj.flush();
            org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'CcfCoreStatus' identifier to no longer be null");
                });
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod
                    .getRandomCcfCoreStatus();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'CcfCoreStatus' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'CcfCoreStatus' failed to provide an identifier");
            obj = com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                    .findCcfCoreStatus(id);
            obj.remove();
            obj.flush();
            org.junit.jupiter.api.Assertions
                    .assertNull(com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus
                                    .findCcfCoreStatus(id), "Failed to remove 'CcfCoreStatus' with identifier '"
                                    + id + "'");
                });
    }
}
