package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplateList;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;

public class FieldMappingExternalAppTemplateAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private FieldMappingExternalAppTemplateDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomFieldMappingExternalAppTemplate(), "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate
                .countFieldMappingExternalAppTemplates();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomFieldMappingExternalAppTemplate(), "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly");
        List<FieldMappingExternalAppTemplate> result = restTemplate
                .getForObject(ccfAPIUrl + "/fieldmappingexternalapptemplates",
                        FieldMappingExternalAppTemplateList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'FieldMappingExternalAppTemplate' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'FieldMappingExternalAppTemplate' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'FieldMappingExternalAppTemplate' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getNewTransientFieldMappingExternalAppTemplate(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'FieldMappingExternalAppTemplate' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/", obj,
                FieldMappingExternalAppTemplate.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj.getId(), "Expected 'FieldMappingExternalAppTemplate' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getRandomFieldMappingExternalAppTemplate();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '"
                                + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'FieldMappingExternalAppTemplate' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                    .getRandomFieldMappingExternalAppTemplate();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/fieldmappingexternalapptemplates/"
                    + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl
                        + "/fieldmappingexternalapptemplates/" + id,
                        FieldMappingExternalAppTemplate.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                .getRandomFieldMappingExternalAppTemplate();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '"
                                + id + "'");
        boolean modified = dod.modifyFieldMappingExternalAppTemplate(obj);
        restTemplate.put(ccfAPIUrl + "/fieldmappingexternalapptemplates/" + id,
                obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappingexternalapptemplates/" + id,
                FieldMappingExternalAppTemplate.class);
        if (obj.getKind() != FieldMappingKind.MAPPING_RULES) {
            Assertions.assertFalse(obj.getRules().isEmpty());
        }
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'FieldMappingExternalAppTemplate' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate obj = dod
                    .getRandomFieldMappingExternalAppTemplate();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'FieldMappingExternalAppTemplate' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'FieldMappingExternalAppTemplate' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/fieldmappingexternalapptemplates/" + id,
                    FieldMappingExternalAppTemplate.class);
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Find method for 'FieldMappingExternalAppTemplate' illegally returned null for id '"
                                    + id + "'");
            restTemplate.put(ccfAPIUrl + "/fieldmappingexternalapptemplates/" + id,
                    obj);
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/fieldmappingexternalapptemplates/" + id + 42,
                    FieldMappingExternalAppTemplate.class);
    
                });
    }

}
