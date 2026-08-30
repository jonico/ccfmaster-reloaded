package com.collabnet.ccf.ccfmaster.server.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.rmi.RemoteException;

import mockit.Mocked;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.collabnet.ccf.ccfmaster.controller.web.UIPathConstants;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ce.soap50.webservices.pluggable.PluggableComponentSoapDO;
import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.filestorage.FileStorageClient;
import com.collabnet.teamforge.api.pluggable.IntegratedApplicationClient;
import com.collabnet.teamforge.api.pluggable.PluggableComponentDO;
import com.collabnet.teamforge.api.pluggable.PluggableComponentParameterDO;
import com.collabnet.teamforge.api.pluggable.PluggablePermissionDO;

public class CreateIntegratedAppStrategyTest {

    private class TestIntegratedApplicationClient extends IntegratedApplicationClient {

        private final PluggableComponentDO integratedApp;

        private TestIntegratedApplicationClient(
                PluggableComponentDO integratedApp) {
            super(connection);
            this.integratedApp = integratedApp;
        }

        @Override
        public PluggableComponentDO createIntegratedApplication(
                String plugName, String description, String baseUrl,
                String goUrl, String prefix, String isScmRequired,
                String requireProjPrefix, String iconFileId, String endPoint,
                PluggableComponentParameterDO[] paramDO, String adminUrl,
                PluggablePermissionDO[] permDO, String pceInputType,
                String pceResultFormat, String pceDescription, String pceTitle)
                throws RemoteException {
            CreateIntegratedAppStrategyTest.this.createCalled = true;
            CreateIntegratedAppStrategyTest.this.adminUrl = adminUrl;
            return integratedApp;
        }

        @Override
        public PluggableComponentDO getIntegratedApplicationByName(String name) {
            CreateIntegratedAppStrategyTest.this.getAppByNameCalled = true;
            return integratedApp;
        }

        @Override
        public void setPluggableAppMessageResource(String plugId,
                String locale, String key, String value) throws RemoteException {
            CreateIntegratedAppStrategyTest.this.setPluggableAppMessageResourceCalled = true;
        }
    }

    boolean    createCalled                         = false;
    boolean    getAppByNameCalled                   = false;
    boolean    setPluggableAppMessageResourceCalled = false;
    String     adminUrl;

    @Mocked
    Connection connection;

    @Test
    public void createCalledAndGetAppByNameNotCalledWhenIADoesNotExist() {
        final Landscape landscape = new Landscape();
        FileStorageClient fileStorageClient = null;
        landscape.setName("TestLandscapeName");
        final String plugId = "plug9999";
        final boolean isCTF8Support = false;
        final PluggableComponentDO integratedApp = new PluggableComponentDO(
                (PluggableComponentSoapDO) null) {
            @Override
            public String getId() {
                return plugId;
            }
        };
        final IntegratedApplicationClient client = new TestIntegratedApplicationClient(
                integratedApp) {
            @Override
            public String getPlugIdByBaseUrl(String baseUrl) {
                return null;
            }
        };

        final String baseUrl = "foo";
        new CreateIntegratedAppStrategy(baseUrl, "iafEndpoint", client,
                fileStorageClient, isCTF8Support).beforeCreate(landscape);
        assertTrue(createCalled, "createIntegratedApplication not called");
        assertFalse(getAppByNameCalled, "getIntegratedApplicationByName was called");
        assertTrue(setPluggableAppMessageResourceCalled, "setPluggableAppMessageResource was not called");
        assertEquals(baseUrl
                + UIPathConstants.CREATELANDSCAPE_CCFMASTER, adminUrl, "bad adminUrl");
        assertEquals(plugId, landscape.getPlugId(), "plugId not set correctly by beforeCreate");

    }

    @BeforeEach
    public void init() {
        createCalled = false;
        getAppByNameCalled = false;
        setPluggableAppMessageResourceCalled = false;
    }

    /* doesn't work; result = integratedApplication always sets to null :( */
    //	@NonStrict IntegratedApplicationClient client;
    //	//@Mocked PluggableComponentDO integratedApplication;
    //
    //	@Test
    //	public void createCalled() throws RemoteException {
    //		final Landscape landscape = new Landscape();
    //		landscape.setDescription("foo");
    //		final String plugId = "plug9999";
    //		final PluggableComponentDO integratedApplication = new PluggableComponentDO((PluggableComponentSoapDO)null) {
    //			@Override
    //			public String getId() { return plugId; }
    //		};
    //		//integratedApplication.setId(plugId);
    //		new Expectations() {
    //			{
    //				client.createIntegratedApplication(anyString, anyString, anyString, anyString, anyString, anyString, anyString, anyString, anyString, (PluggableComponentParameterDO[]) any, anyString, (PluggablePermissionDO[])any, anyString, anyString, anyString, anyString);
    //				client.getIntegratedApplicationByName("foo"); times = 1; result = integratedApplication;
    //				//integratedApplication.getId(); result = plugId;
    //				landscape.setPlugId(plugId); times = 1;
    //			}
    //		};
    //		new CreateIntegratedAppStrategy("foo", client).beforeCreate(landscape);
    //	}
}
