package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeList;

public class LandscapeAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private LandscapeDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomLandscape(), "Data on demand for 'Landscape' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.Landscape
                .countLandscapes();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomLandscape(), "Data on demand for 'Landscape' failed to initialize correctly");
        List<Landscape> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes", LandscapeList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'Landscape' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions.assertNotNull(result, "Find entries method for 'Landscape' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'Landscape' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomLandscape(), "Data on demand for 'Landscape' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getNewTransientLandscape(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Landscape' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'Landscape' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/landscapes/", obj,
                Landscape.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'Landscape' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Landscape' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Landscape' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'Landscape' returned the incorrect identifier");
    }

    @Test
    public void testFindWithPlugId() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Landscape' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Landscape' failed to provide an identifier");
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/landscapes/" + obj.getPlugId(), Landscape.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'Landscape' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                    .getRandomLandscape();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'Landscape' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'Landscape' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/landscapes/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                        Landscape.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                .getRandomLandscape();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Landscape' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Landscape' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Landscape' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyLandscape(obj);
        restTemplate.put(ccfAPIUrl + "/landscapes/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                Landscape.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'Landscape' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.Landscape obj = dod
                    .getRandomLandscape();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'Landscape' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'Landscape' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/landscapes/" + id,
                    Landscape.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Landscape' illegally returned null for id '"
                            + id + "'");
            restTemplate.put(ccfAPIUrl + "/landscapes/" + id + 42, obj);
                });
    }

}
