package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfigList;

public class LandscapeConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private LandscapeConfigDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomLandscapeConfig(), "Data on demand for 'LandscapeConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig
                .countLandscapeConfigs();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomLandscapeConfig(), "Data on demand for 'LandscapeConfig' failed to initialize correctly");
        List<LandscapeConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapeconfigs", LandscapeConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'LandscapeConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'LandscapeConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'LandscapeConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeConfigScope() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod
                .getRandomLandscapeConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'LandscapeConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig
                .countLandscapeConfigsByLandscape(obj.getLandscape());
        List<LandscapeConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/" + obj.getLandscape().getId()
                + "/landscapeconfigs/", LandscapeConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'LandscapeConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'LandscapeConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'LandscapeConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomLandscapeConfig(), "Data on demand for 'LandscapeConfig' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod
                .getNewTransientLandscapeConfig(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'LandscapeConfig' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'LandscapeConfig' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/landscapeconfigs/", obj,
                LandscapeConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'LandscapeConfig' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod
                .getRandomLandscapeConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'LandscapeConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'LandscapeConfig' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/" + id,
                LandscapeConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'LandscapeConfig' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'LandscapeConfig' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod
                    .getRandomLandscapeConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'LandscapeConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'LandscapeConfig' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/landscapeconfigs/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/"
                        + id, LandscapeConfig.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod
                .getRandomLandscapeConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'LandscapeConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'LandscapeConfig' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/" + id,
                LandscapeConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'LandscapeConfig' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyLandscapeConfig(obj);
        restTemplate.put(ccfAPIUrl + "/landscapeconfigs/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/" + id,
                LandscapeConfig.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'LandscapeConfig' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig obj = dod
                    .getRandomLandscapeConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'LandscapeConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'LandscapeConfig' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/landscapeconfigs/" + id,
                    LandscapeConfig.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'LandscapeConfig' illegally returned null for id '"
                            + id + "'");
            dod.modifyLandscapeConfig(obj);
            //Test with wrong id
            restTemplate.put(ccfAPIUrl + "/landscapeconfigs/" + id + 42, obj);
    
                });
    }

}
