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
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomFieldMapping(), "Data on demand for 'FieldMapping' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.FieldMapping
                .countFieldMappings();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomFieldMapping(), "Data on demand for 'FieldMapping' failed to initialize correctly");
        List<FieldMapping> result = restTemplate.getForObject(ccfAPIUrl
                + "/fieldmappings", FieldMappingList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'FieldMapping' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'FieldMapping' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'FieldMapping' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                .getNewTransientFieldMapping(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMapping' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'FieldMapping' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/fieldmappings/", obj,
                FieldMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'FieldMapping' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                .getRandomFieldMapping();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMapping' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'FieldMapping' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/fieldmappings/" + id,
                FieldMapping.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'FieldMapping' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'FieldMapping' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                    .getRandomFieldMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'FieldMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'FieldMapping' failed to provide an identifier");
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
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'FieldMapping' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'FieldMapping' failed to provide an identifier");
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
        if (FieldMappingKind.MAPPING_RULES != obj.getKind()) {
            Assertions.assertFalse(obj.getRules().isEmpty());
        }
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'FieldMapping' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.FieldMapping obj = dod
                    .getRandomFieldMapping();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'FieldMapping' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'FieldMapping' failed to provide an identifier");
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
