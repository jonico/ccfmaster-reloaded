package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionList;

public class RepositoryMappingDirectionAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomRepositoryMappingDirection(), "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirections();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomRepositoryMappingDirection(), "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappingdirections",
                RepositoryMappingDirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'RepositoryMappingDirection' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries");
    }

    @Test
    public void testCountDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByDirection(obj.getDirection());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappingdirections/"
                        + obj.getDirection() + "/",
                RepositoryMappingDirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'RepositoryMappingDirection' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeAndDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByLandscapeAndDirection(
                        obj.getRepositoryMapping().getExternalApp()
                                .getLandscape(), obj.getDirection());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl
                        + "/landscapes/"
                        + obj.getRepositoryMapping().getExternalApp()
                                .getLandscape().getPlugId()
                        + "/repositorymappingdirections/" + obj.getDirection(),
                RepositoryMappingDirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'RepositoryMappingDirection' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection
                .countRepositoryMappingDirectionsByLandscape(obj
                        .getRepositoryMapping().getExternalApp().getLandscape());
        List<RepositoryMappingDirection> result = restTemplate.getForObject(
                ccfAPIUrl
                        + "/landscapes/"
                        + obj.getRepositoryMapping().getExternalApp()
                                .getLandscape().getPlugId()
                        + "/repositorymappingdirections",
                RepositoryMappingDirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'RepositoryMappingDirection' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'RepositoryMappingDirection' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'RepositoryMappingDirection' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getNewTransientRepositoryMappingDirection(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'RepositoryMappingDirection' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl
                + "/repositorymappingdirections/", obj,
                RepositoryMappingDirection.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj.getId(), "Expected 'RepositoryMappingDirection' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'RepositoryMappingDirection' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                    .getRandomRepositoryMappingDirection();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/repositorymappingdirections/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl
                        + "/repositorymappingdirections/" + id,
                        RepositoryMappingDirection.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                .getRandomRepositoryMappingDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyRepositoryMappingDirection(obj);
        restTemplate.put(ccfAPIUrl + "/repositorymappingdirections/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/repositorymappingdirections/" + id,
                RepositoryMappingDirection.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'RepositoryMappingDirection' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection obj = dod
                    .getRandomRepositoryMappingDirection();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirection' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMappingDirection' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/repositorymappingdirections/" + id,
                    RepositoryMappingDirection.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMappingDirection' illegally returned null for id '"
                            + id + "'");
            //put with wrong id
            restTemplate.put(ccfAPIUrl + "/repositorymappingdirections/" + id + 42,
                    obj);
                });
    }

}
