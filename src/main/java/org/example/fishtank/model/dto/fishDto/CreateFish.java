package org.example.fishtank.model.dto.fishDto;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateFish(
        @NonNull
        String name,
        @NonNull
        String species,
        String description,
        @NonNull
        String waterType,
        @NonNull
        String sex,
        @NonNull
        String appUser,
        @NonNull
        String image) {
}
