package org.example.fishtank.model.dto.appUserDto.security;

public record LoginAppUser(String id, String name, String password, String email, String authenticationCode, String access) {
}
