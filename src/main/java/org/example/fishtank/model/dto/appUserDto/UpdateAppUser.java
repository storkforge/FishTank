package org.example.fishtank.model.dto.appUserDto;

import org.springframework.lang.NonNull;

public record UpdateAppUser(
        @NonNull
        String name,
        @NonNull
        String email,
        @NonNull
        String passwordHash,
        @NonNull
        String access) {
}
