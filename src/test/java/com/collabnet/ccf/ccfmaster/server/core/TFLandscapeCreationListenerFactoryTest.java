package com.collabnet.ccf.ccfmaster.server.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TFLandscapeCreationListenerFactoryTest {
    private static final String BASE_URL = "http://localhost:8080/CCFMaster";

    @Test
    public void slashIsAppendedToBaseUrlTFEACLF() {
        TFExternalAppCreationListenerFactory factory = new TFExternalAppCreationListenerFactory(
                BASE_URL);
        assertTrue(factory.baseUrl.endsWith("/"), "baseUrl must end with '/'");
        assertFalse(factory.baseUrl.endsWith("//"), "unnecessary extra slash was appended");
    }

    @Test
    public void slashIsAppendedToBaseUrlTFLCLF() {
        TFLandscapeCreationListenerFactory factory = new TFLandscapeCreationListenerFactory();
        factory.setBaseUrl(BASE_URL);
        assertTrue(factory.getBaseUrl().endsWith("/"), "baseUrl must end with '/'");
        assertFalse(factory
                .getBaseUrl().endsWith("//"), "unnecessary extra slash was appended");
    }

    @Test
    public void slashIsNotAppendedToBaseUrlUnlessNecessaryTFEACLF() {
        TFExternalAppCreationListenerFactory factory = new TFExternalAppCreationListenerFactory(
                BASE_URL + "/");
        assertTrue(factory.baseUrl.endsWith("/"), "baseUrl must end with '/'");
        assertFalse(factory.baseUrl.endsWith("//"), "unnecessary extra slash was appended");
    }

    @Test
    public void slashIsNotAppendedToBaseUrlUnlessNecessaryTFLCLF() {
        TFLandscapeCreationListenerFactory factory = new TFLandscapeCreationListenerFactory();
        factory.setBaseUrl(BASE_URL + "/");
        assertTrue(factory.getBaseUrl().endsWith("/"), "baseUrl must end with '/'");
        assertFalse(factory
                .getBaseUrl().endsWith("//"), "unnecessary extra slash was appended");
    }
}
