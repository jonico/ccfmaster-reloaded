package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;

public class IdentityMappingAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private IdentityMappingDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomIdentityMapping(), "Data on demand for 'IdentityMapping' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping
                .countIdentityMappings();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomIdentityMapping(), "Data on demand for 'IdentityMapping' failed to initialize correctly");
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl
                + "/identitymappings", IdentityMappingList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'IdentityMapping' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'IdentityMapping' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'IdentityMapping' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeScope() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping
                .countIdentityMappingsByLandscape(obj.getRepositoryMapping()
                        .getExternalApp().getLandscape());
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/"
                + obj.getRepositoryMapping().getExternalApp().getLandscape()
                        .getPlugId() + "/identitymappings",
                IdentityMappingList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'IdentityMapping' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'IdentityMapping' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'IdentityMapping' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomIdentityMapping(), "Data on demand for 'IdentityMapping' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getNewTransientIdentityMapping(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'IdentityMapping' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/identitymappings/", obj,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'IdentityMapping' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'IdentityMapping' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'IdentityMapping' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                    .getRandomIdentityMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'IdentityMapping' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/identitymappings/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/"
                        + id, IdentityMapping.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'IdentityMapping' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyIdentityMapping(obj);
        restTemplate.put(ccfAPIUrl + "/identitymappings/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'IdentityMapping' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                    .getRandomIdentityMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'IdentityMapping' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                    IdentityMapping.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'IdentityMapping' illegally returned null for id '"
                            + id + "'");
            restTemplate.put(ccfAPIUrl + "/identitymappings/" + id + 42, obj);
                });
    }

}
