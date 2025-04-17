package org.example.fishtank.service;

import org.example.fishtank.util.FileStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Spy
    @InjectMocks
    ImageService imageService;

    @Mock
    private Clock clock;
    @Mock
    private FileStorage fileStorage;
    String testImageName = "test_Image.png";
    private final String testUploadDir = "test/uploads";
    Instant fixedInstant = Instant.ofEpochMilli(123456789L);

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

    @Test
    @DisplayName("Sanitized should throw exception when fileName contain unwanted symbols")
    void sanitizedShouldThrowExceptionWhenFileNameContainUnwantedSymbols() {
        String fileNameWithUnwantedSymbols = "/test..Image\\.png";

        assertThrows(IllegalArgumentException.class, () -> imageService.sanitizeFileName(fileNameWithUnwantedSymbols));
    }

    @Test
    @DisplayName("Sanitized should replace spaces with underscore in fileName")
    void sanitizedShouldReplaceSpacesWithUnderscoreInFileName() {
        String fileNameSpaces = "test Image.png";

        assertThat(imageService.sanitizeFileName(fileNameSpaces)).isEqualTo(testImageName);
    }

    @Test
    @DisplayName("GenerateUniqueFileName should return originalFileName with timestamp")
    void generateUniqueFileNameShouldReturnOriginalFileNameWithTimestamp() {
        when(clock.instant()).thenReturn(fixedInstant);

        String expectedName = "123456789" + "_" + testImageName;
        assertThat(imageService.generateUniqueFileName(testImageName)).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("BuildSafeFilePath should return resolved path when filename is valid")
    void buildSafeFilePathShouldReturnResolvedPathWhenFilenameIsValid() {
        Path baseDir = Paths.get(testUploadDir);
        String fileName = "photo.jpg";

        Path result = imageService.buildSafeFilePath(baseDir, fileName);

        assertThat(result).isEqualTo(baseDir.resolve(fileName).normalize());
    }

    @Test
    @DisplayName("BuildSafeFilePath should throw exception when filename is trying to escape baseDir")
    void buildSafeFilePathShouldThrowExceptionWhenFilenameIsTryingToEscapeBaseDir() {
        Path baseDir = Paths.get(testUploadDir);
        String fileName = "../etc/passwd";

        assertThatThrownBy(() -> imageService.buildSafeFilePath(baseDir, fileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid filename");
    }

    @Test
    @DisplayName("SaveImage should coordinate saving process correctly")
    void saveImageShouldCoordinateSavingProcessCorrectly() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(testImageName);

        Path uploadPath = Paths.get(testUploadDir);
        String generatedFileName = "123456_test_Image.png";
        Path safePath = uploadPath.resolve(generatedFileName);

        doReturn(uploadPath).when(imageService).prepareUploadDirectory();
        doReturn(generatedFileName).when(imageService).generateUniqueFileName(testImageName);
        doReturn(safePath).when(imageService).buildSafeFilePath(uploadPath, generatedFileName);

        String result = imageService.saveImage(file);

        assertThat(result).isEqualTo(generatedFileName);
        verify(imageService).prepareUploadDirectory();
        verify(imageService).generateUniqueFileName(testImageName);
        verify(imageService).buildSafeFilePath(uploadPath, generatedFileName);
        verify(fileStorage).save(safePath, file);
    }

    @Test
    @DisplayName("SaveImage should throw IOException when file storage fails")
    void saveImageShouldThrowIOExceptionWhenFileStorageFails() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(testImageName);

        Path uploadPath = Paths.get(testUploadDir);
        String generatedFileName = "123456_test_Image.png";
        Path safePath = uploadPath.resolve(generatedFileName);

        doReturn(uploadPath).when(imageService).prepareUploadDirectory();
        doReturn(generatedFileName).when(imageService).generateUniqueFileName(testImageName);
        doReturn(safePath).when(imageService).buildSafeFilePath(uploadPath, generatedFileName);

        doThrow(new IOException("File saving failed")).when(fileStorage).save(safePath, file);

        assertThatThrownBy(() -> imageService.saveImage(file))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("File saving failed");

        verify(imageService).prepareUploadDirectory();
        verify(imageService).generateUniqueFileName(testImageName);
        verify(imageService).buildSafeFilePath(uploadPath, generatedFileName);
        verify(fileStorage).save(safePath, file);
    }

}