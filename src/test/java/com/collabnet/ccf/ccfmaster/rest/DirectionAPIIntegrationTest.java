package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionList;

public class DirectionAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private DirectionDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomDirection(), "Data on demand for 'Direction' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction
                .countDirections();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomDirection(), "Data on demand for 'Direction' failed to initialize correctly");
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl
                + "/directions", DirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'Direction' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions.assertNotNull(result, "Find entries method for 'Direction' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'Direction' returned an incorrect number of entries");
    }

    @Test
    public void testCountDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                .getRandomDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction
                .countDirectionsByDirection(obj.getDirection());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl
                + "/directions/" + obj.getDirection() + "/",
                DirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'Direction' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions.assertNotNull(result, "Find entries method for 'Direction' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'Direction' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeAndDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                .getRandomDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction
                .countDirectionsByLandscapeAndDirection(obj.getLandscape(),
                        obj.getDirection());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/" + obj.getLandscape().getPlugId()
                + "/directions/" + obj.getDirection(), DirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'Direction' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions.assertNotNull(result, "Find entries method for 'Direction' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'Direction' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeScope() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                .getRandomDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.Direction
                .countDirectionsByLandscapeEquals(obj.getLandscape());
        List<Direction> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/" + obj.getLandscape().getPlugId()
                + "/directions", DirectionList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'Direction' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions.assertNotNull(result, "Find entries method for 'Direction' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'Direction' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                .getNewTransientDirection(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'Direction' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/directions/", obj,
                Direction.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'Direction' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                .getRandomDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/" + id,
                Direction.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Direction' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'Direction' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                    .getRandomDirection();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/directions/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/directions/" + id,
                        Direction.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                .getRandomDirection();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/" + id,
                Direction.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Direction' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyDirection(obj);
        restTemplate.put(ccfAPIUrl + "/directions/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/directions/" + id,
                Direction.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'Direction' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.Direction obj = dod
                    .getRandomDirection();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'Direction' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/directions/" + id,
                    Direction.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Direction' illegally returned null for id '"
                            + id + "'");
            //put with wrong id
            restTemplate.put(ccfAPIUrl + "/directions/" + id + 42, obj);
    
                });
    }

}
