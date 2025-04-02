package org.example.fishtank.model.dto.postDto;

public record ResponsePost(
        Integer id,
        String text,
        Integer fishId) {
}
