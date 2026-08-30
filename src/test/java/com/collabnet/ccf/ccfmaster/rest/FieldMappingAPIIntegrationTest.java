package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingList;

public class FieldMappingAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private FieldMappingDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        dod.getRandomFieldMapping());
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMapping
                .countFieldMappings();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        dod.getRandomFieldMapping());
        List<FieldMapping> result = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappings", FieldMappingList.class);
        org.junit.Assert
                .assertTrue(
                        "Counter for 'FieldMapping' incorrectly reported there were no entries",
                        count > 0);
        org.junit.Assert
                .assertNotNull(
                        "Find entries method for 'FieldMapping' illegally returned null",
                        result);
        org.junit.Assert
                .assertEquals(
                        "Find entries method for 'FieldMapping' returned an incorrect number of entries",
                        count, result.size());
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                .getNewTransientFieldMapping(Integer.MAX_VALUE);
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide a new transient entity",
                        obj);
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'FieldMapping' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/fieldmappings/", obj,
                FieldMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'FieldMapping' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                .getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                FieldMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'FieldMapping' illegally returned null for id '"
                        + id + "'");
        org.junit.Assert
                .assertEquals(
                        "Find method for 'FieldMapping' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                    .getRandomFieldMapping();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'FieldMapping' failed to initialize correctly",
                            obj);
            java.lang.Long id = obj.getId();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'FieldMapping' failed to provide an identifier",
                            id);
            restTemplate.delete(ccfAPIUrl + "/fieldmappings/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                        FieldMapping.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                .getRandomFieldMapping();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'FieldMapping' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                FieldMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'FieldMapping' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyFieldMapping(obj);
        restTemplate.put(ccfAPIUrl + "/fieldmappings/" + id, obj);
        //        obj = obj.merge();
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                FieldMapping.class);
        if (obj.getKind() != FieldMappingKind.MAPPING_RULES) {
            Assertions.assertFalse(obj.getRules().isEmpty());
        }
        org.junit.Assert
                .assertTrue(
                        "Version for 'FieldMapping' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                    .getRandomFieldMapping();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'FieldMapping' failed to initialize correctly",
                            obj);
            java.lang.Long id = obj.getId();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'FieldMapping' failed to provide an identifier",
                            id);
            obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                    FieldMapping.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'FieldMapping' illegally returned null for id '"
                            + id + "'");
            dod.modifyFieldMapping(obj);
            restTemplate.put(ccfAPIUrl + "/fieldmappings/" + id, obj);
            //wrong id
            obj = restTemplate.getForObject(
                    ccfAPIUrl + "/fieldmappings/" + id + 42, FieldMapping.class);
    
                });
    }

}
