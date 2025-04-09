package org.example.fishtank.model.dto.fishDto;

import org.springframework.lang.NonNull;

import java.io.Serializable;

public record ResponseFish (
        @NonNull
        Integer id,
        String name,
        String species,
        String description,
        String waterType,
        String sex,
        String appUser,
        String image)
        implements Serializable {
}