package org.example.fishtank.model.dto;

public record CreateAppUser (String name,String email, String passwordHash, String access){
}
