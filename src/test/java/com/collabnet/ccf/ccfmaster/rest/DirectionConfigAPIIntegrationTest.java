package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionConfigList;

public class DirectionConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private DirectionConfigDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomDirectionConfig(), "Data on demand for 'DirectionConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig
                .countDirectionConfigs();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomDirectionConfig(), "Data on demand for 'DirectionConfig' failed to initialize correctly");
        List<DirectionConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/directionconfigs", DirectionConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'DirectionConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'DirectionConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'DirectionConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCountDirectionConfigScope() {
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod
                .getRandomDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'DirectionConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig
                .countDirectionConfigsByDirection(obj.getDirection());
        List<DirectionConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/directions/" + obj.getDirection().getId()
                + "/directionconfigs/", DirectionConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'DirectionConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'DirectionConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'DirectionConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomDirectionConfig(), "Data on demand for 'DirectionConfig' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod
                .getNewTransientDirectionConfig(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'DirectionConfig' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'DirectionConfig' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/directionconfigs/", obj,
                DirectionConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'DirectionConfig' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod
                .getRandomDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'DirectionConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'DirectionConfig' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/" + id,
                DirectionConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'DirectionConfig' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'DirectionConfig' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod
                    .getRandomDirectionConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'DirectionConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'DirectionConfig' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/directionconfigs/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/"
                        + id, DirectionConfig.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod
                .getRandomDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'DirectionConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'DirectionConfig' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/" + id,
                DirectionConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'DirectionConfig' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyDirectionConfig(obj);
        restTemplate.put(ccfAPIUrl + "/directionconfigs/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/" + id,
                DirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'DirectionConfig' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.DirectionConfig obj = dod
                    .getRandomDirectionConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'DirectionConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'DirectionConfig' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/directionconfigs/" + id,
                    DirectionConfig.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'DirectionConfig' illegally returned null for id '"
                            + id + "'");
            dod.modifyDirectionConfig(obj);
            //put to ressource with wrong id
            restTemplate.put(ccfAPIUrl + "/directionconfigs/" + id + 42, obj);
    
                });
    }

}
