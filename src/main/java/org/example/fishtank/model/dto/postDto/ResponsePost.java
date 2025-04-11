package org.example.fishtank.model.dto.postDto;

import java.io.Serializable;

public record ResponsePost(
        Integer id,
        String text,
        String cityName,
        Double longitude,
        Double latitude,
        Integer fishId)
        implements Serializable {
}
