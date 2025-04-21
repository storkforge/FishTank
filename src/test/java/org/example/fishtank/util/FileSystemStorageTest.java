package org.example.fishtank.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemStorageTest {

    private final FileSystemStorage fileSystemStorage = new FileSystemStorage();

    @Test
    void testSave() throws IOException {
        byte[] content = "Hello, this is a test".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", content);
        Path tempFile = Files.createTempFile("upload-test-", ".txt");

        fileSystemStorage.save(tempFile, mockFile);

        assertTrue(Files.exists(tempFile));
        byte[] savedContent = Files.readAllBytes(tempFile);
        assertTrue(new String(savedContent).equals("Hello, this is a test"));

        Files.deleteIfExists(tempFile);
    }
}
