package com.collabnet.ccf.ccfmaster.rest;

import mockit.Mocked;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.core.CoreStateMachine;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus.CoreState;
import com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatusDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.DirectionDataOnDemand;

public class CcfCoreStatusAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private DirectionDataOnDemand     dirdod;
    @Autowired
    private CcfCoreStatusDataOnDemand dod;
    @Mocked
    CoreStateMachine                  stateMachine;

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            Direction dir = dirdod.getRandomDirection();
    
            CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'CcfCoreStatus' failed to provide a new transient entity",
                            obj);
            obj.setId(null);
            obj.setVersion(null);
            org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'CcfCoreStatus' identifier to be null");
            try {
                obj = restTemplate.postForObject(ccfAPIUrl + "/ccfcorestatuses/",
                        obj, CcfCoreStatus.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(401, e.getStatusCode().value());
                throw e;
            }
            org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'CcfCoreStatus' identifier to no longer be null");
                });
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Direction dir = dirdod
                .getRandomDirection();
        CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatusesByDirection(dir)
                .getSingleResult();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'CcfCoreStatus' failed to initialize correctly",
                        obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'CcfCoreStatus' failed to provide an identifier",
                        id);
        obj = restTemplate.getForObject(ccfAPIUrl + "/ccfcorestatuses/" + id,
                CcfCoreStatus.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'CcfCoreStatus' illegally returned null for id '"
                        + id + "'");
        org.junit.Assert
                .assertEquals(
                        "Find method for 'CcfCoreStatus' returned the incorrect identifier",
                        id, obj.getId());
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            final Direction dir = dirdod.getRandomDirection();
            final CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
            //        final com.collabnet.ccf.ccfmaster.server.domain.CcfCoreStatus obj = dod.getRandomCcfCoreStatus();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'CcfCoreStatus' failed to initialize correctly",
                            obj);
            java.lang.Long id = obj.getId();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'CcfCoreStatus' failed to provide an identifier",
                            id);
            try {
                restTemplate.delete(ccfAPIUrl + "/ccfcorestatuses/" + id);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(401, e.getStatusCode().value());
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        final Direction dir = dirdod.getRandomDirection();
        final CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'CcfCoreStatus' failed to initialize correctly",
                        obj);
        new mockit.NonStrictExpectations() {
            {
                stateMachine.determineCurrentStatus((CcfCoreStatus) any, 0);
                returns(CoreState.STOPPED);
                stateMachine.merge((CcfCoreStatus) any);
                returns(obj);
            }
        };
        java.lang.Long id = obj.getId();
        org.junit.Assert
                .assertNotNull(
                        "Data on demand for 'CcfCoreStatus' failed to provide an identifier",
                        id);
        java.lang.Integer currentVersion = obj.getVersion();
        CcfCoreStatus obj2 = restTemplate.getForObject(ccfAPIUrl
                + "/ccfcorestatuses/" + id, CcfCoreStatus.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'CcfCoreStatus' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyCcfCoreStatus(obj2);
        restTemplate.put(ccfAPIUrl + "/ccfcorestatuses/" + id, obj2);
        obj2 = restTemplate.getForObject(ccfAPIUrl + "/ccfcorestatuses/" + id,
                CcfCoreStatus.class);
        org.junit.Assert
                .assertTrue(
                        "Version for 'CcfCoreStatus' failed to increment on flush directive",
                        (currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified);
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            final Direction dir = dirdod.getRandomDirection();
            final CcfCoreStatus obj = CcfCoreStatus.findCcfCoreStatus(dir.getId());
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'CcfCoreStatus' failed to initialize correctly",
                            obj);
            new mockit.NonStrictExpectations() {
                {
                    stateMachine.determineCurrentStatus((CcfCoreStatus) any, 0);
                    returns(CoreState.STOPPED);
                    stateMachine.merge((CcfCoreStatus) any);
                    returns(obj);
                }
            };
            java.lang.Long id = obj.getId();
            org.junit.Assert
                    .assertNotNull(
                            "Data on demand for 'CcfCoreStatus' failed to provide an identifier",
                            id);
            CcfCoreStatus obj2 = restTemplate.getForObject(ccfAPIUrl
                    + "/ccfcorestatuses/" + id, CcfCoreStatus.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'CcfCoreStatus' illegally returned null for id '"
                            + id + "'");
            //test with wrong id
            restTemplate.put(ccfAPIUrl + "/ccfcorestatuses/" + id + 42, obj2);
                });
    }

}
