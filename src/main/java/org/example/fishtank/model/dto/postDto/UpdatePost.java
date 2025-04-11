package org.example.fishtank.model.dto.postDto;

import org.springframework.lang.NonNull;

public record UpdatePost(
        @NonNull
        String text) {
}
