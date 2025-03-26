package org.example.fishtank.model.entity.fishDTO;

import org.springframework.lang.NonNull;

public record ResponseFish (
        @NonNull
        Integer id,
        String name,
        String species,
        String description,
        String waterType,
        String sex,
        String appUser){
}