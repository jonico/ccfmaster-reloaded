package com.collabnet.ccf.ccfmaster.rest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantList;

public class ParticipantAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private ParticipantDataOnDemand dod;

    @Test
    public void testCount() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomParticipant(), "Data on demand for 'Participant' failed to initialize correctly");
        long count = com.collabnet.ccf.ccfmaster.server.domain.Participant
                .countParticipants();
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomParticipant(), "Data on demand for 'Participant' failed to initialize correctly");
        List<Participant> result = restTemplate.getForObject(ccfAPIUrl
                + "/participants", ParticipantList.class);
        org.junit.jupiter.api.Assertions
                .assertTrue(count > 0, "Counter for 'Participant' incorrectly reported there were no entries");
        org.junit.jupiter.api.Assertions
                .assertNotNull(result, "Find entries method for 'Participant' illegally returned null");
        org.junit.jupiter.api.Assertions
                .assertEquals(count, result.size(), "Find entries method for 'Participant' returned an incorrect number of entries");
    }

    @Test
    public void testCreate() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(dod.getRandomParticipant(), "Data on demand for 'Participant' failed to initialize correctly");
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod
                .getNewTransientParticipant(Integer.MAX_VALUE);
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Participant' failed to provide a new transient entity");
        org.junit.jupiter.api.Assertions.assertNull(obj.getId(), "Expected 'Participant' identifier to be null");
        obj = restTemplate.postForObject(ccfAPIUrl + "/participants/", obj,
                Participant.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj.getId(), "Expected 'Participant' identifier to no longer be null");
    }

    @Test
    public void testFind() {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod
                .getRandomParticipant();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Participant' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Participant' failed to provide an identifier");
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/" + id,
                Participant.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Participant' illegally returned null for id '"
                        + id + "'");
        org.junit.jupiter.api.Assertions
                .assertEquals(id, obj.getId(), "Find method for 'Participant' returned the incorrect identifier");
    }

    @Test
    public void testRemove() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod
                    .getRandomParticipant();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'Participant' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'Participant' failed to provide an identifier");
            restTemplate.delete(ccfAPIUrl + "/participants/" + id);
            try {
                obj = restTemplate.getForObject(ccfAPIUrl + "/participants/" + id,
                        Participant.class);
            } catch (HttpClientErrorException e) {
                Assertions.assertEquals(404, e.getStatusCode().value(), "Expected 404");
                throw e;
            }
                });
    }

    @Test
    public void testUpdate() {
        com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod
                .getRandomParticipant();
        org.junit.jupiter.api.Assertions
                .assertNotNull(obj, "Data on demand for 'Participant' failed to initialize correctly");
        java.lang.Long id = obj.getId();
        org.junit.jupiter.api.Assertions
                .assertNotNull(id, "Data on demand for 'Participant' failed to provide an identifier");
        java.lang.Integer currentVersion = obj.getVersion();
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/" + id,
                Participant.class);
        org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Participant' illegally returned null for id '"
                        + id + "'");
        boolean modified = dod.modifyParticipant(obj);
        restTemplate.put(ccfAPIUrl + "/participants/" + id, obj);
        obj = restTemplate.getForObject(ccfAPIUrl + "/participants/" + id,
                Participant.class);
        org.junit.jupiter.api.Assertions
                .assertTrue((currentVersion != null && obj.getVersion() > currentVersion)
                                || !modified, "Version for 'Participant' failed to increment on flush directive");
    }

    @Test
    public void testWrongUpdate() {
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientErrorException.class, () -> {    
            com.collabnet.ccf.ccfmaster.server.domain.Participant obj = dod
                    .getRandomParticipant();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(obj, "Data on demand for 'Participant' failed to initialize correctly");
            java.lang.Long id = obj.getId();
            org.junit.jupiter.api.Assertions
                    .assertNotNull(id, "Data on demand for 'Participant' failed to provide an identifier");
            obj = restTemplate.getForObject(ccfAPIUrl + "/participants/" + id,
                    Participant.class);
            org.junit.jupiter.api.Assertions.assertNotNull(obj, "Find method for 'Participant' illegally returned null for id '"
                            + id + "'");
            //test with wrong id
            restTemplate.put(ccfAPIUrl + "/participants/" + id + 42, obj);
                });
    }

}
