package com.collabnet.ccf.ccfmaster.server.core.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.collabnet.ccf.ccfmaster.config.Version;
import com.google.common.io.Closeables;
import com.google.common.io.Resources;

import static org.junit.jupiter.api.Assertions.*;

public class CoreZipFileTest {

    private static final String VALID_ZIP_FILE_NAME   = "valid.zip";
    private static final String INVALID_ZIP_FILE_NAME = "invalid.zip";
    private static final String BAD_ZIP_FILE_NAME     = "bad.zip";

    // JUnit 5 has no @Rule; @TempDir is the replacement for TemporaryFolder. It injects
    // the directory itself rather than a rule object, so getRoot() becomes the field and
    // newFile(name) becomes an explicit File + createNewFile().
    @TempDir
    File                        folder;

    private File                propFile              = null;

    @BeforeEach
    public void init() throws IOException {
        propFile = new File(folder, CoreProperties.META_INFORMATION_FILENAME);
        propFile.createNewFile();
    }

    @Test
    public void unzipBadZipFileThrowsZipFileException() throws ZipException,
            IOException {
        org.junit.jupiter.api.Assertions.assertThrows(ZipException.class, () -> {    
            CoreZipFile czf = null;
            try {
                czf = createCoreZipFile(BAD_ZIP_FILE_NAME);
            } finally {
                // Guava dropped Closeables.closeQuietly(Closeable); close(x, true) is
                // the replacement that swallows IOException
                Closeables.close(czf, true);
            }
                });
    }

    @Test
    public void unzipCreatesNewFiles() throws IOException {
        writeCorePropFile(propFile, new Version(0, 0, 0, ""), "foo");
        CoreZipFile czf = null;
        boolean threw = true;
        try {
            czf = createCoreZipFile(VALID_ZIP_FILE_NAME);
            assertNotNull(czf, "creating CoreZipFile failed.");
            assertTrue(czf.validate(), "validating valid zip file failed");
            final File root = folder;
            int before = root.list().length;
            czf.unzipTo(root);
            int after = root.list().length;
            assertTrue(before < after, "no new files in " + root);
            threw = false;
        } finally {
            Closeables.close(czf, threw);
        }
    }

    @Test
    public void unzipDoesntOverwriteNewerLandscape() throws IOException {
        org.junit.jupiter.api.Assertions.assertThrows(CoreUpdateException.class, () -> {    
            writeCorePropFile(propFile, new Version(200, 0, 0, ""), "foo");
            CoreZipFile czf = null;
            boolean threw = true;
            try {
                czf = createCoreZipFile(VALID_ZIP_FILE_NAME);
                czf.unzipTo(folder);
                threw = false;
            } finally {
                Closeables.close(czf, threw);
            }
                });
    }

    @Test
    public void unzipInvalidZipFileDoesNotValidate() throws ZipException,
            IOException {
        CoreZipFile czf = null;
        boolean threw = true;
        try {
            czf = createCoreZipFile(INVALID_ZIP_FILE_NAME);
            assertNotNull(czf, "creating CoreZipFile failed");
            assertFalse(czf.validate(), "validating invalid zip should fail");
            threw = false;
        } finally {
            Closeables.close(czf, threw);
        }
    }

    private void writeCorePropFile(File propFile, Version version, String desc)
            throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.setProperty(Version.CCFCORE_MAJOR_VERSION,
                Integer.toString(version.getMajor()));
        props.setProperty(Version.CCFCORE_MINOR_VERSION,
                Integer.toString(version.getMinor()));
        props.setProperty(Version.CCFCORE_PATCH_VERSION,
                Integer.toString(version.getPatch()));
        props.setProperty(Version.CCFCORE_REVISION_STRING,
                version.getRevision());
        props.setProperty(CoreProperties.CCFCORE_DESCRIPTION, desc);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(propFile);
            props.store(out, null);
        } finally {
            if (out != null)
                out.close();
        }
    }

    CoreZipFile createCoreZipFile(String fileName) throws IOException,
            ZipException {
        URL testCoreZip = Resources
                .getResource(CoreZipFileTest.class, fileName);
        // Guava replaced InputSupplier with ByteSource
        MultipartFile upload = new MockMultipartFile("file", Resources
                .asByteSource(testCoreZip).openStream());
        return CoreZipFile.fromMultipartFile(upload);
    }

}
