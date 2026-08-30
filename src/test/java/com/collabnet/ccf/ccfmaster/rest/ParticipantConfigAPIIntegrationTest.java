package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfigList;

public class ParticipantConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private ParticipantConfigDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomParticipantConfig(), "Data on demand for 'ParticipantConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig
                .countParticipantConfigs();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomParticipantConfig(), "Data on demand for 'ParticipantConfig' failed to initialize correctly");
        List<ParticipantConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/participantconfigs", ParticipantConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'ParticipantConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'ParticipantConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'ParticipantConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCountParticipantConfigScope() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ParticipantConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig
                .countParticipantConfigsByParticipant(obj.getParticipant());
        List<ParticipantConfig> result = restTemplate.getForObject(ccfAPIUrl
                + "/participants/" + obj.getParticipant().getId()
                + "/participantconfigs/", ParticipantConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'ParticipantConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'ParticipantConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'ParticipantConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomParticipantConfig(), "Data on demand for 'ParticipantConfig' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getNewTransientParticipantConfig(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ParticipantConfig' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'ParticipantConfig' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/participantconfigs/",
                obj, ParticipantConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'ParticipantConfig' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ParticipantConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'ParticipantConfig' failed to provide an identifier");
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ParticipantConfig' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'ParticipantConfig' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                    .getRandomParticipantConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'ParticipantConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'ParticipantConfig' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/participantconfigs/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/participantconfigs/"
                        + id, ParticipantConfig.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                .getRandomParticipantConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ParticipantConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'ParticipantConfig' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ParticipantConfig' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyParticipantConfig(obj);
        restTemplate.put(ccfAPIUrl + "/participantconfigs/" + id, obj);
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/participantconfigs/" + id,
                ParticipantConfig.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'ParticipantConfig' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig obj = dod
                    .getRandomParticipantConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'ParticipantConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'ParticipantConfig' failed to provide an identifier");
            obj = restTemplate.getForObject(
                    ccfAPIUrl + "/participantconfigs/" + id,
                    ParticipantConfig.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ParticipantConfig' illegally returned null for id '"
                            + id + "'");
            dod.modifyParticipantConfig(obj);
            //put to ressource with wrong id
            restTemplate.put(ccfAPIUrl + "/participantconfigs/" + id + 42, obj);
    
                });
    }

}
