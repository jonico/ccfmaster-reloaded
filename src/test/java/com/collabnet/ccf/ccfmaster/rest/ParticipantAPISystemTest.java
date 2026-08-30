package com.collabnet.ccf.ccfmaster.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.collabnet.ccf.ccfmaster.server.domain.ParticipantList;

public class ParticipantAPISystemTest extends AbstractAPISystemTest {

    @Test
    public void participantsExist() {
        ParticipantList participants = restTemplate.getForObject(ccfAPIUrl
                + "/participants", ParticipantList.class);
        assertTrue(!participants.isEmpty(), "no participants returned");
        assertTrue(true);
    }

}
