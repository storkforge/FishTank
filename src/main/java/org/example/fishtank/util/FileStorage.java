package org.example.fishtank.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorage {
    void save(Path path, MultipartFile file) throws IOException;
}
