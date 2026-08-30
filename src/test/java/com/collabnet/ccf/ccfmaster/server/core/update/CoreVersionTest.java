package com.collabnet.ccf.ccfmaster.server.core.update;

import org.junit.jupiter.api.Test;

import com.collabnet.ccf.ccfmaster.config.Version;

import static org.junit.jupiter.api.Assertions.*;

public class CoreVersionTest {

    @Test
    public void everythingIsGreaterThanNull() {
        Version v = new Version(0, 0, 0, "");
        assertTrue(v.isNewerThan(null), "any version should be newer than null");
    }

    @Test
    public void majorTrumpsMinor() {
        Version v1 = new Version(1, 0, 0, "");
        Version v2 = new Version(0, 1, 0, "");
        assertTrue(v1.isNewerThan(v2), "major should trump minor version");
    }

    @Test
    public void minorDecidesEqualMajor() {
        Version v1 = new Version(1, 1, 0, "");
        Version v2 = new Version(1, 0, 0, "");
        assertTrue(v1.isNewerThan(v2), "minor decides when major versions are equal");
    }

    @Test
    public void minorTrumpsPatch() {
        Version v1 = new Version(0, 1, 0, "");
        Version v2 = new Version(0, 0, 1, "");
        assertTrue(v1.isNewerThan(v2), "minor should trump patch version");
    }

    @Test
    public void patchDecidesEqualMinor() {
        Version v1 = new Version(0, 1, 1, "");
        Version v2 = new Version(0, 1, 0, "");
        assertTrue(v1.isNewerThan(v2), "patch decides when minor versions are equal");
    }
}
