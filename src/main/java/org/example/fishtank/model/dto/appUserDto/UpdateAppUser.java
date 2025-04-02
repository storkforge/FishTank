package org.example.fishtank.model.dto.appUserDto;

public record UpdateAppUser(String name,String email, String passwordHash, String access) {
}
