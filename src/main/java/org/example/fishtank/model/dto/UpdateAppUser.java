package org.example.fishtank.model.dto;

public record UpdateAppUser(String name,String email, String passwordHash, String access) {
}
