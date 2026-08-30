package com.collabnet.ccf.ccfmaster.authorization;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegratedAppVoterTest {

    final static Pattern  pattern         = Pattern.compile("prpl\\d+");

    final static String[] validApiPaths   = { "/api/linkid/prpl1234/foobar",
            "/api/linkid/prpl1/foobar",
            "/api/linkid/prpl12346786789678686/foobar",
            "/api/linkid/prpl1234/foobar", "/linkid/prpl1234/foobar",
            "/linkid/prpl1/foobar", "/linkid/prpl12346786789678686/foobar",
            "/linkid/prpl1234/foobar",   };

    final static String[] invalidApiPaths = {
            "/otherprefix/linkid/prpl1234",
            "/api/linkId/prpl1234/foobar", // capitalization
            "/api/linkid/something1234/foobar", "/api/linkid/prplabcd",
            "/api/linkid/prpl",
            "/api/linkid/prpl1234", // no slash at the end
            "/linkId/prpl1234/foobar", // capitalization
            "/linkid/something1234/foobar", "/linkid/prplabcd", "/linkid/prpl",
            "/linkid/prpl1234", // no slash at the end
                                          };

    @Test
    public void invalidApiUrls() {
        for (String path : invalidApiPaths)
            assertNull(IntegratedAppVoter.findLinkIdInUrl(path), "accepted path: " + path);
    }

    @Test
    public void validApiUrls() {
        for (String path : validApiPaths) {
            final String linkId = IntegratedAppVoter.findLinkIdInUrl(path);
            assertNotNull(linkId, "rejected path: " + path);
            assertTrue(pattern
                    .matcher(linkId).matches(), "bad linkId (" + linkId + ")in path: " + path);
        }
    }

}
