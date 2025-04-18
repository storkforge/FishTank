package org.example.fishtank.model.dto.postDto;

import org.springframework.lang.NonNull;

public record CreatePost(
        @NonNull
        String text,
        String cityName,
        @NonNull
        Integer fishId) {
}
