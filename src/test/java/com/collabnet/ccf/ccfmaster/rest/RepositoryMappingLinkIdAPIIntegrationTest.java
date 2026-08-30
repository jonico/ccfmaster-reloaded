package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingList;

public class RepositoryMappingLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDataOnDemand dod;

    @Autowired
    private ExternalAppDataOnDemand       dodEA;

    @Test
    public void testCount() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping
                .countRepositoryMappingsByExternalApp(obj.getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        List<RepositoryMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, RepositoryMappingList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'RepositoryMapping' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'RepositoryMapping' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'RepositoryMapping' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping randomObject = dod
                .getRandomRepositoryMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(randomObject, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
        String linkIdPathSegment = "/linkid/"
                + randomObject.getExternalApp().getLinkId()
                + "/repositorymappings/";
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getNewTransientRepositoryMapping(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'RepositoryMapping' identifier to be null");
        obj.setExternalApp(randomObject.getExternalApp());
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
                RepositoryMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'RepositoryMapping' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMapping' failed to provide an identifier");
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'RepositoryMapping' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                    .getRandomRepositoryMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMapping' failed to provide an identifier");
            String linkIdPathSegment = "/linkid/"
                    + obj.getExternalApp().getLinkId() + "/repositorymappings/";
            restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                        RepositoryMapping.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testReparenting() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                    .getRandomRepositoryMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMapping' failed to provide an identifier");
            obj = restTemplate.getForObject(
                    ccfAPIUrl + "/repositorymappings/" + id,
                    RepositoryMapping.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMapping' illegally returned null for id '"
                            + id + "'");
            ExternalApp ea = dodEA.getNewTransientExternalApp(42);
            ea.persist();
            obj.setExternalApp(ea);
            String linkIdPathSegment = "/linkid/"
                    + obj.getExternalApp().getLinkId() + "/repositorymappings/";
    
            try {
                restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(403, e.getStatusCode().value(), "Expected 403");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                .getRandomRepositoryMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMapping' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/repositorymappings/" + id,
                RepositoryMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMapping' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyRepositoryMapping(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getExternalApp().getLinkId() + "/repositorymappings/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMapping.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'RepositoryMapping' failed to increment on flush directive");
    }

    @Test
    public void testWithWrongParentIdInPath() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                    .getRandomRepositoryMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMapping' failed to provide an identifier");
            obj = restTemplate.getForObject(
                    ccfAPIUrl + "/repositorymappings/" + id,
                    RepositoryMapping.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMapping' illegally returned null for id '"
                            + id + "'");
            ExternalApp ea = dodEA.getNewTransientExternalApp(42);
            ea.persist();
            String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                    + "/repositorymappings/";
    
            try {
                restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(403, e.getStatusCode().value(), "Expected 403");
                throw e;
            }
                });
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = dod
                    .getRandomRepositoryMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMapping' failed to provide an identifier");
            obj = restTemplate.getForObject(
                    ccfAPIUrl + "/repositorymappings/" + id,
                    RepositoryMapping.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'RepositoryMapping' illegally returned null for id '"
                            + id + "'");
            String linkIdPathSegment = "/linkid/"
                    + obj.getExternalApp().getLinkId() + "/repositorymappings/";
            //put wrong id
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id + 42, obj);
                });
    }

}
