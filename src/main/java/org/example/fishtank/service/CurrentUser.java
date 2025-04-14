package org.example.fishtank.service;

import org.example.fishtank.repository.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public final class CurrentUser {

    private final AppUserRepository appUserRepository;

    public CurrentUser(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public static int getId(){
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();
        return Integer.parseInt(userIdString);
    }

    public String getUsername(){
        var id = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getName();
    }

    public String getRole(){
        var id = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getAccess().getName();
    }
}
