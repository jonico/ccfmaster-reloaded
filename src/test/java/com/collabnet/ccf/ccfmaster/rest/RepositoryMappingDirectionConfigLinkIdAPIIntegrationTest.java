package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfigList;

public class RepositoryMappingDirectionConfigLinkIdAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private RepositoryMappingDirectionConfigDataOnDemand dodRMDC;

    @Autowired
    private ExternalAppDataOnDemand                      dodEA;

    @Test
    public void testCountByRepositoryMappingDirectionScope() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig
                .countRepositoryMappingDirectionConfigsByExternalAppAndRepositoryMappingDirection(
                        obj.getRepositoryMappingDirection()
                                .getRepositoryMapping().getExternalApp(),
                        obj.getRepositoryMappingDirection());

        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirections/"
                + obj.getRepositoryMappingDirection().getId()
                + "/repositorymappingdirectionconfigs/";
        List<RepositoryMappingDirectionConfig> result = restTemplate
                .getForObject(ccfAPIUrl + linkIdPathSegment,
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
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig randomconfigObject = dodRMDC
                .getNewTransientRepositoryMappingDirectionConfig(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(randomconfigObject, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        String linkIdPathSegment = "/linkid/"
                + randomconfigObject.getRepositoryMappingDirection()
                        .getRepositoryMapping().getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";

        randomconfigObject = restTemplate.postForObject(ccfAPIUrl
                + linkIdPathSegment, randomconfigObject,
                RepositoryMappingDirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(randomconfigObject.getId(), "Expected 'RepositoryMappingDirectionConfig' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                .getRandomRepositoryMappingDirectionConfig();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier");
        // figure out linkId path segment
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
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
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
                    .getRandomRepositoryMappingDirectionConfig();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'RepositoryMappingDirectionConfig' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'RepositoryMappingDirectionConfig' failed to provide an identifier");
            String linkIdPathSegment = "/linkid/"
                    + obj.getRepositoryMappingDirection().getRepositoryMapping()
                            .getExternalApp().getLinkId()
                    + "/repositorymappingdirectionconfigs/";
            restTemplate.delete(ccfAPIUrl + linkIdPathSegment + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                        RepositoryMappingDirectionConfig.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
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
        boolean modified = dodRMDC.modifyRepositoryMappingDirectionConfig(obj);
        String linkIdPathSegment = "/linkid/"
                + obj.getRepositoryMappingDirection().getRepositoryMapping()
                        .getExternalApp().getLinkId()
                + "/repositorymappingdirectionconfigs/";
        restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + linkIdPathSegment + id,
                RepositoryMappingDirectionConfig.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'RepositoryMappingDirectionConfig' failed to increment on flush directive");
    }

    @Test
    public void testWithWrongGrandparentIdInPath() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirectionConfig obj = dodRMDC
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
            ExternalApp ea = dodEA.getNewTransientExternalApp(42);
            ea.persist();
            String linkIdPathSegment = "/linkid/" + ea.getLinkId()
                    + "/repositorymappingdirectionconfigs/";
    
            try {
                restTemplate.put(ccfAPIUrl + linkIdPathSegment + id, obj);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(403, e.getStatusCode().value(), "Expected 403");
                throw e;
            }
                });
    }

}
