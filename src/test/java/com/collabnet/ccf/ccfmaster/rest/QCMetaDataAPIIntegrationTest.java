package com.collabnet.ccf.ccfmaster.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.LandscapeDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.ParticipantConfig;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;

public class QCMetaDataAPIIntegrationTest extends AbstractAPIIntegrationTest {

    @Autowired
    private LandscapeDataOnDemand dod;

    private String                qcURL           = "http://localhost:9090/qcbin/";
    private String                domain          = "DEFAULT";
    private String                qcUser          = "admin";
    private String                qcPassword      = "admin";
    private String                project         = "CCFDemo";
    private String                requirementType = "Functional";

    private Landscape             landscape;

    @BeforeEach
    public void prepareLandscape() {
        landscape = dod.getRandomLandscape();
        Participant participant = landscape.getParticipant();
        participant.setSystemKind(SystemKind.QC);
        participant.merge();
        ParticipantConfig pc = new ParticipantConfig();
        pc.setParticipant(participant);
        pc.setVal(qcURL);
        pc.setName("ccf.participant.qc.url");
        pc.merge();
    }

    @Test
    public void testShowDefectFields() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + domain + "/projects/" + project
                + "/defectFields/?qcUser=" + qcUser + "&qcPassword="
                + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions.assertEquals(result, "returned string did not match expectations", "showDefectFields" + landscape + domain + project + qcURL
                        + qcUser + qcPassword);
    }

    @Test
    public void testShowRequirementFields() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + domain + "/projects/" + project
                + "/requirementTypes/" + requirementType
                + "/requirementFields/?qcUser=" + qcUser + "&qcPassword="
                + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions
                .assertEquals("showRequirementFields" + landscape + domain + project
                                + requirementType + qcURL + qcUser + qcPassword, result, "returned string did not match expectations");
    }

    @Test
    public void testShowRequirementTypes() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + domain + "/projects/" + project
                + "/requirementTypes/?qcUser=" + qcUser + "&qcPassword="
                + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions.assertEquals(result, "returned string did not match expectations", "showRequirementTypes" + landscape + domain + project + qcURL
                        + qcUser + qcPassword);
    }

    @Test
    public void testShowVisibleDomains() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + "?qcUser=" + qcUser + "&qcPassword="
                + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions.assertEquals(result, "returned string did not match expectations", "showVisibleDomains" + landscape + qcURL + qcUser + qcPassword);
    }

    @Test
    public void testShowVisibleProjectsInDomain() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + domain + "/projects/?qcUser="
                + qcUser + "&qcPassword=" + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions.assertEquals(result, "returned string did not match expectations", "showVisibleProjectsInDomain" + landscape + domain + qcURL
                        + qcUser + qcPassword);
    }

    @Test
    public void testValidateDomainAndProject() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + domain + "/projects/" + project
                + "?qcUser=" + qcUser + "&qcPassword=" + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions.assertEquals(result, "returned string did not match expectations", "validateDomainAndProject" + landscape + domain + project
                        + qcURL + qcUser + qcPassword);
    }

    @Test
    public void testValidateDomainAndProjectAndRequirementType() {
        org.junit.jupiter.api.Assertions
                .assertNotNull(landscape, "Data on demand for 'Landscape' failed to initialize correctly");
        String apiString = "/landscapes/" + landscape.getPlugId()
                + "/qcmetadata/domains/" + domain + "/projects/" + project
                + "/requirementTypes/" + requirementType + "?qcUser=" + qcUser
                + "&qcPassword=" + qcPassword;
        String result = restTemplate.getForObject(ccfAPIUrl + apiString,
                String.class);
        org.junit.jupiter.api.Assertions.assertEquals(result, "returned string did not match expectations", "validateDomainAndProjectAndRequirementType" + landscape
                        + domain + project + requirementType + qcURL + qcURL
                        + qcPassword);
    }

}
