package org.example.fishtank.model.dto.postDto;

import java.io.Serializable;

public record ResponsePost(
        Integer id,
        String text,
        Integer fishId)
        implements Serializable {
}
