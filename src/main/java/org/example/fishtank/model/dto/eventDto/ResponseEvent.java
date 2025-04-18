package org.example.fishtank.model.dto.eventDto;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ResponseEvent (
        @NonNull
        Integer id,
        String title,
        String text,
        String cityName,
        LocalDateTime eventDate,
        Integer appUserId)
        implements Serializable {
}
