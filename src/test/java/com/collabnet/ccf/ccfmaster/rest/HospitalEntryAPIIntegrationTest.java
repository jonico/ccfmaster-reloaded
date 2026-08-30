package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.HospitalEntryList;

public class HospitalEntryAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private HospitalEntryDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomHospitalEntry(), "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrys();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomHospitalEntry(), "Data on demand for 'HospitalEntry' failed to initialize correctly");
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + "/hospitalentrys/", HospitalEntryList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByDirection(obj
                        .getRepositoryMappingDirection().getDirection());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + "/hospitalentrys/"
                + obj.getRepositoryMappingDirection().getDirection() + "/",
                HospitalEntryList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountDirectionScopeWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByDirection(obj
                        .getRepositoryMappingDirection().getDirection());
        String result = restTemplate.getForObject(ccfAPIUrl
                + "/hospitalentrys/"
                + obj.getRepositoryMappingDirection().getDirection()
                + "/count/", String.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, Long.parseLong(result), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeAndDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByLandscapeAndDirection(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape(), obj
                        .getRepositoryMappingDirection().getDirection());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape().getPlugId()
                + "/hospitalentrys/"
                + obj.getRepositoryMappingDirection().getDirection(),
                HospitalEntryList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeAndDirectionScopeWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByLandscapeAndDirection(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape(), obj
                        .getRepositoryMappingDirection().getDirection());
        String result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape().getPlugId()
                + "/hospitalentrys/"
                + obj.getRepositoryMappingDirection().getDirection()
                + "/count/", String.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, Long.parseLong(result), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeScope() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByLandscape(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape());
        List<HospitalEntry> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape().getPlugId()
                + "/hospitalentrys", HospitalEntryList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeScopeWithCountMethod() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrysByLandscape(obj
                        .getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape());
        String result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLandscape().getPlugId()
                + "/hospitalentrys/count/", String.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, Long.parseLong(result), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCountWithCountMethod() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomHospitalEntry(), "Data on demand for 'HospitalEntry' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry
                .countHospitalEntrys();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomHospitalEntry(), "Data on demand for 'HospitalEntry' failed to initialize correctly");
        String result = restTemplate.getForObject(ccfAPIUrl
                + "/hospitalentrys/count/", String.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'HospitalEntry' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'HospitalEntry' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, Long.parseLong(result), "Find entries method for 'HospitalEntry' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomHospitalEntry(), "Data on demand for 'HospitalEntry' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getNewTransientHospitalEntry(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'HospitalEntry' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/hospitalentrys/", obj,
                HospitalEntry.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'HospitalEntry' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'HospitalEntry' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/" + id,
                HospitalEntry.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'HospitalEntry' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'HospitalEntry' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                    .getRandomHospitalEntry();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'HospitalEntry' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/hospitalentrys/" + id);
            try {
                obj = restTemplate.getForObject(
                        ccfAPIUrl + "/hospitalentrys/" + id, HospitalEntry.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                .getRandomHospitalEntry();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'HospitalEntry' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/" + id,
                HospitalEntry.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'HospitalEntry' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyHospitalEntry(obj);
        restTemplate.put(ccfAPIUrl + "/hospitalentrys/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/" + id,
                HospitalEntry.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'HospitalEntry' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.HospitalEntry obj = dod
                    .getRandomHospitalEntry();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'HospitalEntry' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'HospitalEntry' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/hospitalentrys/" + id,
                    HospitalEntry.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'HospitalEntry' illegally returned null for id '"
                            + id + "'");
            //test with wrong id
            restTemplate.put(ccfAPIUrl + "/hospitalentrys/" + id + 42, obj);
                });
    }

}
