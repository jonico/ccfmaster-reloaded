package com.collabnet.ccf.ccfmaster.rest;

import java.io.IOException;

import jakarta.xml.bind.JAXBException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.config.CoreConfigLoader;
import com.collabnet.ccf.ccfmaster.server.domain.CCFCorePropertyList;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;

public class CcfCorePropertyListAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private DirectionDataOnDemand dirdod;

    @Autowired
    private CoreConfigLoader      coreConfigLoader;

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            try {
                Direction dir = dirdod.getRandomDirection();
                Assertions.assertNotNull(dir, "Data on demand for 'Direction' failed to initialize correctly");
                Long id = dir.getId();
                Assertions.assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
                CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                        + "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
                restTemplate.postForObject(ccfAPIUrl + "/ccfcoreproperties", obj,
                        CCFCorePropertyList.class);
            } catch (HttpClientErrorException e) {
                // create operation is considered to be a bad request
                Assertions.assertEquals(401, e.getStatusCode().value());
                throw e;
            }
                });
    }

    @Test
    public void testFind() throws JAXBException, IOException {
        Direction dir = dirdod.getRandomDirection();
        Assertions.assertNotNull(dir, "Data on demand for 'Direction' failed to initialize correctly");
        Long id = dir.getId();
        Assertions.assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
        CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                + "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
        Assertions.assertNotNull(obj, "Find method for 'CCFCorePropertyList' illegally returned null for id '"
                        + id + "'");
        Assertions.assertNotNull(coreConfigLoader
                .getDefaultCCFCorePropertyList(dir));
        Assertions.assertEquals(obj.getCcfCoreProperties().size(), coreConfigLoader
                .getDefaultCCFCorePropertyList(dir).size());

    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            Direction dir = dirdod.getRandomDirection();
            Assertions.assertNotNull(dir, "Data on demand for 'Direction' failed to initialize correctly");
            Long id = dir.getId();
            Assertions.assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
            try {
                restTemplate.delete(ccfAPIUrl + "/ccfcoreproperties/" + id);
            } catch (HttpClientErrorException e) {
                // delete operation is considered to be a bad request
                Assertions.assertEquals(401, e.getStatusCode().value());
                throw e;
            }
                });
    }

    @Test
    public void testShowWithInvalidId() throws JAXBException, IOException {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            try {
                CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                        + "/ccfcoreproperties/101", CCFCorePropertyList.class);
                Assertions.assertNull(obj);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value());
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            Direction dir = dirdod.getRandomDirection();
            Assertions.assertNotNull(dir, "Data on demand for 'Direction' failed to initialize correctly");
            Long id = dir.getId();
            Assertions.assertNotNull(id, "Data on demand for 'Direction' failed to provide an identifier");
            try {
                CCFCorePropertyList obj = restTemplate.getForObject(ccfAPIUrl
                        + "/ccfcoreproperties/" + id, CCFCorePropertyList.class);
                Assertions.assertNotNull(obj, "Find method for 'CCFCorePropertyList' illegally returned null for id '"
                                + id + "'");
                restTemplate.put(ccfAPIUrl + "/ccfcoreproperties/" + id, obj);
            } catch (HttpClientErrorException e) {
                // update operation is considered to be a bad request
                Assertions.assertEquals(401, e.getStatusCode().value());
                throw e;
            }
                });
    }
}
