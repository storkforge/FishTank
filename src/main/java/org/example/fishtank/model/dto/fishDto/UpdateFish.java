package org.example.fishtank.model.dto.fishDto;

import org.springframework.lang.NonNull;

public record UpdateFish(
        @NonNull
        String name,
        @NonNull
        String description
) {
}
