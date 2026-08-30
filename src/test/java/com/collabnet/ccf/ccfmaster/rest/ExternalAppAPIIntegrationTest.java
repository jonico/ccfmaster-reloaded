package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppList;

public class ExternalAppAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private ExternalAppDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomExternalApp(), "Data on demand for 'ExternalApp' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.ExternalApp
                .countExternalApps();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomExternalApp(), "Data on demand for 'ExternalApp' failed to initialize correctly");
        List<ExternalApp> result = restTemplate.getForObject(ccfAPIUrl
                + "/externalapps", ExternalAppList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'ExternalApp' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'ExternalApp' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'ExternalApp' returned an incorrect number of entries");
    }

    @Test
    public void testCountLandscapeScope() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                .getRandomExternalApp();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.ExternalApp
                .countExternalAppsByLandscape(obj.getLandscape());
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomExternalApp(), "Data on demand for 'ExternalApp' failed to initialize correctly");
        List<ExternalApp> result = restTemplate.getForObject(ccfAPIUrl
                + "/landscapes/" + obj.getLandscape().getPlugId()
                + "/externalapps", ExternalAppList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'ExternalApp' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'ExternalApp' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'ExternalApp' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomExternalApp(), "Data on demand for 'ExternalApp' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                .getNewTransientExternalApp(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'ExternalApp' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/externalapps/", obj,
                ExternalApp.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'ExternalApp' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                .getRandomExternalApp();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'ExternalApp' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/" + id,
                ExternalApp.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ExternalApp' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'ExternalApp' returned the incorrect identifier");
    }

    @Test
    public void testFindWithLinkId() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                .getRandomExternalApp();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'ExternalApp' failed to provide an identifier");
        obj = restTemplate.getForObject(
                ccfAPIUrl + "/externalapps/" + obj.getLinkId(),
                ExternalApp.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ExternalApp' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'ExternalApp' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                    .getRandomExternalApp();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'ExternalApp' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/externalapps/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/" + id,
                        ExternalApp.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                .getRandomExternalApp();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'ExternalApp' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/" + id,
                ExternalApp.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ExternalApp' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyExternalApp(obj);
        restTemplate.put(ccfAPIUrl + "/externalapps/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/" + id,
                ExternalApp.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'ExternalApp' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.ExternalApp obj = dod
                    .getRandomExternalApp();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'ExternalApp' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'ExternalApp' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/externalapps/" + id,
                    ExternalApp.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'ExternalApp' illegally returned null for id '"
                            + id + "'");
            restTemplate.put(ccfAPIUrl + "/externalapps/" + id + 42, obj);
                });
    }

}
