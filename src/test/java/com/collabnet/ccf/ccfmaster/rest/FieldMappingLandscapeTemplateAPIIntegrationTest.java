package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplateDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplateList;

public class FieldMappingLandscapeTemplateAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private FieldMappingLandscapeTemplateDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomFieldMappingLandscapeTemplate(), "Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate
                .countFieldMappingLandscapeTemplates();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomFieldMappingLandscapeTemplate(), "Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly");
        List<FieldMappingLandscapeTemplate> result = restTemplate.getForObject(
                ccfAPIUrl + "/fieldmappinglandscapetemplates",
                FieldMappingLandscapeTemplateList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'FieldMappingLandscapeTemplate' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'FieldMappingLandscapeTemplate' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'FieldMappingLandscapeTemplate' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod
                .getNewTransientFieldMappingLandscapeTemplate(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMappingLandscapeTemplate' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions
                .assertNull(obj.getId(), "Expected 'FieldMappingLandscapeTemplate' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl
                + "/fieldmappinglandscapetemplates/", obj,
                FieldMappingLandscapeTemplate.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj.getId(), "Expected 'FieldMappingLandscapeTemplate' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod
                .getRandomFieldMappingLandscapeTemplate();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappinglandscapetemplates/" + id,
                FieldMappingLandscapeTemplate.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Find method for 'FieldMappingLandscapeTemplate' illegally returned null for id '"
                                + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'FieldMappingLandscapeTemplate' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod
                    .getRandomFieldMappingLandscapeTemplate();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier");
            restTemplate
                    .delete(ccfAPIUrl + "/fieldmappinglandscapetemplates/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl
                        + "/fieldmappinglandscapetemplates/" + id,
                        FieldMappingLandscapeTemplate.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod
                .getRandomFieldMappingLandscapeTemplate();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappinglandscapetemplates/" + id,
                FieldMappingLandscapeTemplate.class);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Find method for 'FieldMappingLandscapeTemplate' illegally returned null for id '"
                                + id + "'");
        boolean modified = dod.modifyFieldMappingLandscapeTemplate(obj);
        restTemplate.put(ccfAPIUrl + "/fieldmappinglandscapetemplates/" + id,
                obj);
        obj = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappinglandscapetemplates/" + id,
                FieldMappingLandscapeTemplate.class);
        if (FieldMappingKind.MAPPING_RULES != obj.getKind()) {
            Assertions.assertFalse(obj.getRules().isEmpty());
        }
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'FieldMappingLandscapeTemplate' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate obj = dod
                    .getRandomFieldMappingLandscapeTemplate();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'FieldMappingLandscapeTemplate' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'FieldMappingLandscapeTemplate' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl
                    + "/fieldmappinglandscapetemplates/" + id,
                    FieldMappingLandscapeTemplate.class);
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Find method for 'FieldMappingLandscapeTemplate' illegally returned null for id '"
                                    + id + "'");
            //test with wrong id
            restTemplate.put(ccfAPIUrl + "/fieldmappinglandscapetemplates/" + id
                    + 42, obj);
                });
    }

}
