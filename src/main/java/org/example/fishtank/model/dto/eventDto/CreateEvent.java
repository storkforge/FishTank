package org.example.fishtank.model.dto.eventDto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record CreateEvent(@NonNull String text, String cityName, LocalDateTime eventDate, @NonNull Integer appUserId) {
    public CreateEvent {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or blank");
        }
        if (appUserId == null) {
            throw new IllegalArgumentException("AppUserId cannot be null");
        }
    }
}