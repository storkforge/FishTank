package org.example.fishtank.model.dto.eventDto;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record UpdateEvent(@NonNull
                          String text,
                          String cityName,
                          LocalDateTime eventDate) {
}
