package org.example.fishtank.model.entity.fishDTO;

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
