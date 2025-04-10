package org.example.fishtank.model.dto.appUserDto;

import org.springframework.lang.NonNull;

public record CreateAppUser (
        @NonNull
        String name,
        @NonNull
        String email,
        @NonNull
        String password,
        @NonNull
        String access){
}
