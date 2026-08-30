package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigList;

public class RepositoryMappingDirectionConfigAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionConfigDataOnDemand rdod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(rdod.getRandomRepositoryMappingDirectionConfig(), "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig
                .countRepositoryMappingDirectionConfigs();
        org.junit.jupiter.api.Assertions
                .assertNotNull(rdod.getRandomRepositoryMappingDirectionConfig(), "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        List<RepositoryMappingDirectionConfig> result = restTemplate
                .getForObject(
                        ccfAPIUrl + "/repositorymappingdirectionconfigs/",
                        RepositoryMappingDirectionConfigList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'RepositoryMappingDirectionConfig' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'RepositoryMappingDirectionConfig' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'RepositoryMappingDirectionConfig' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(rdod.getRandomRepositoryMappingDirectionConfig(), "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
                .getNewTransientRepositoryMappingDirectionConfig(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'RepositoryMappingDirectionConfig' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/", obj,
                RepositoryMappingDirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj.getId(), "Expected 'RepositoryMappingDirectionConfig' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/" + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Find method for 'RepositoryMappingDirectionConfig' illegally returned null for id '"
                                + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'RepositoryMappingDirectionConfig' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
                    .getRandomRepositoryMappingDirectionConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/repositorymappingdirectionconfigs/"
                    + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl
                        + "/repositorymappingdirectionconfigs/" + id,
                        RepositoryMappingDirectionConfig.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/" + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Find method for 'RepositoryMappingDirectionConfig' illegally returned null for id '"
                                + id + "'");
        boolean modified = rdod.modifyRepositoryMappingDirectionConfig(obj);
        restTemplate.put(ccfAPIUrl + "/repositorymappingdirectionconfigs/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirectionconfigs/" + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'RepositoryMappingDirectionConfig' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = rdod
                    .getRandomRepositoryMappingDirectionConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/repositorymappingdirectionconfigs/" + id,
                    RepositoryMappingDirectionConfig.class);
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Find method for 'RepositoryMappingDirectionConfig' illegally returned null for id '"
                                    + id + "'");
            rdod.modifyRepositoryMappingDirectionConfig(obj);
            //put to ressource with wrong id
            restTemplate.put(ccfAPIUrl + "/repositorymappingdirectionconfigs/" + id
                    + 42, obj);
    
                });
    }
}
