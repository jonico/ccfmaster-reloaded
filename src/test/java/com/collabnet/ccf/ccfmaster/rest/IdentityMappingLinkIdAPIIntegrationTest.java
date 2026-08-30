package com.collabnet.ccf.ccfmaster.rest;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.IdentityMappingList;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDataOnDemand;

public class IdentityMappingLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private IdentityMappingDataOnDemand   dod;

    @Autowired
    private ExternalAppDataOnDemand       dodEA;

    @Autowired
    private RepositoryMappingDataOnDemand dodRM;

    @Test
    public void testCount() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping
                .countIdentityMappingsByExternalApp(obj.getRepositoryMapping()
                        .getExternalApp());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, IdentityMappingList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'IdentityMapping' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'IdentityMapping' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'IdentityMapping' returned an incorrect number of entries");
    }

    @Test
    public void testCountRepositoryMappingScope() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getRandomIdentityMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping
                .countIdentityMappingsByRepositoryMapping(obj
                        .getRepositoryMapping());
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappings/" + obj.getRepositoryMapping().getId()
                + "/identitymappings/";
        List<IdentityMapping> result = restTemplate.getForObject(ccfAPIUrl
                + linkIdPathSegment, IdentityMappingList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'IdentityMapping' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'IdentityMapping' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'IdentityMapping' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping randomObject = dod
                .getRandomIdentityMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(randomObject, "Data on demand for 'IdentityMapping' failed to initialize correctly");
        String linkIdPathSegment = "/linkid/"
                + randomObject.getRepositoryMapping().getExternalApp()
                        .getLinkId() + "/identitymappings/";
        com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping obj = dod
                .getNewTransientIdentityMapping(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'IdentityMapping' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'IdentityMapping' identifier to be null");
        obj.setRepositoryMapping(randomObject.getRepositoryMapping());
        obj = restTemplate.postForObject(ccfAPIUrl + linkIdPathSegment, obj,
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
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'IdentityMapping' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'IdentityMapping' returned the incorrect identifier");
    }

    @Test
    public void testReGrandparenting() {
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
            ExternalApp ea = dodEA.getNewTransientExternalApp(42);
            ea.persist();
    
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'IdentityMapping' illegally returned null for id '"
                            + id + "'");
            String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                    + "/identitymappings/";
    
            try {
                restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(403, e.getStatusCode().value(), "Expected 403");
                throw e;
            }
                });
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
            String linkIdPathSegment = "/linkid/"
                    + obj.getRepositoryMapping().getExternalApp().getLinkId()
                    + "/identitymappings/";
            restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                        IdentityMapping.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testReparenting() {
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
            RepositoryMapping rm = dodRM.getNewTransientRepositoryMapping(42);
            rm.persist();
            obj.setRepositoryMapping(rm);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'IdentityMapping' illegally returned null for id '"
                            + id + "'");
            String linkIdPathSegment = "/linkid/"
                    + obj.getRepositoryMapping().getExternalApp().getLinkId()
                    + "/identitymappings/";
    
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
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMapping().getExternalApp().getLinkId()
                + "/identitymappings/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'IdentityMapping' failed to increment on flush directive");
        // now let's test whether immutable values can be updated as well
        obj = restTemplate.getForObject(ccfAPIUrl + "/identitymappings/" + id,
                IdentityMapping.class);
        obj.setSourceArtifactId("foo");
        obj.setTargetArtifactId("bar");
        Date currentDate = new Date();
        obj.setTargetLastModificationTime(currentDate);
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                IdentityMapping.class);
        org.junit.jupiter.api.Assertions.assertFalse("foo".equals(obj.getSourceArtifactId()), "source artifact id should be immutable");
        org.junit.jupiter.api.Assertions.assertFalse("bar".equals(obj.getTargetArtifactId()), "target artifact id should be immutable");
        org.junit.jupiter.api.Assertions.assertTrue(currentDate.equals(obj.getTargetLastModificationTime()), "last modification time should be changeable");
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
            String linkIdPathSegment = "/linkid/"
                    + obj.getRepositoryMapping().getExternalApp().getLinkId()
                    + "/identitymappings/";
            //test with wrong id
            restTemplate.put(ccfAPIUrl + linkIdPathSegment + id + 42, obj);
                });
    }

}
