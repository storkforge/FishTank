package org.example.fishtank.service;

import org.example.fishtank.util.FileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;

@Service
public class ImageService {

    private final Clock clock;
    @Value("${file.upload-dir}")
    private String uploadDir;
    private FileStorage fileStorage;

    public ImageService(Clock clock, FileStorage fileStorage) {
        this.clock = clock;
        this.fileStorage = fileStorage;
    }

    public String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = prepareUploadDirectory();

        String fileName = generateUniqueFileName(file.getOriginalFilename());

        Path filePath = buildSafeFilePath(uploadPath, fileName);

        fileStorage.save(filePath, file);

        return fileName;
    }

    public Path prepareUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }

    public Path getImagePath(String filename) {
        return buildSafeFilePath(Paths.get(uploadDir), filename);
    }

    public String sanitizeFileName(String originalFileName) {
        if (originalFileName == null) throw new IllegalArgumentException("Filename is null");
        String sanitized = originalFileName.replaceAll("\\s+", "_");
        if (sanitized.contains("..") || sanitized.contains("/") || sanitized.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename");
        }
        return sanitized;
    }

    public Path buildSafeFilePath(Path baseDir, String fileName) {
        Path path = baseDir.resolve(fileName).normalize();
        if (!path.startsWith(baseDir)) {
            throw new IllegalArgumentException("Invalid filename");
        }
        return path;
    }

    public String generateUniqueFileName(String originalFileName) {
        String sanitized = sanitizeFileName(originalFileName);
        return Instant.now(clock).toEpochMilli() + "_" + sanitized;
    }
}
