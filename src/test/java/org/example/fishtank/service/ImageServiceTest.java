package org.example.fishtank.service;

import org.example.fishtank.repository.FishRepository;
import org.example.fishtank.util.FileStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @Mock
    private FileStorage fileStorage;
    String imageTest = "testImage.png";
    private final String testUploadDir = "test/uploads";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void cleanUp() throws IOException {
        Path path = Path.of("test/uploads");
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    @DisplayName("PrepareUploadDirectory should return uploadPath if directory exists")
    void prepareUploadDirectoryShouldReturnUploadPathIfDirectoryExists() throws IOException {
        Files.createDirectory(Path.of(testUploadDir));
        ReflectionTestUtils.setField(imageService, "uploadDir", testUploadDir);

        Path expectedPath = Path.of(testUploadDir);
        Path actualPath = imageService.prepareUploadDirectory();

        assertThat(actualPath).isEqualTo(expectedPath);
        assertTrue(Files.exists(actualPath), "Directory should exist after preparation");
    }

    @Test
    @DisplayName("PrepareUploadDirectory should return uploadPath if directory does not exist")
    void prepareUploadDirectoryShouldReturnUploadPathIfDirectoryDoesNotExist() throws IOException {
        ReflectionTestUtils.setField(imageService, "uploadDir", testUploadDir);

        Path expectedPath = Path.of(testUploadDir);
        Path actualPath = imageService.prepareUploadDirectory();

        assertThat(actualPath).isEqualTo(expectedPath);
        assertTrue(Files.exists(actualPath), "Directory should exist after preparation");
    }




}