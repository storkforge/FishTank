package org.example.fishtank.model.dto.fishDto;

import org.springframework.lang.NonNull;

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
        String appUser
) {
}
