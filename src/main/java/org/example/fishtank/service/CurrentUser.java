package org.example.fishtank.service;

import org.example.fishtank.exception.custom.CurrentUserException;
import org.example.fishtank.repository.AppUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public final class CurrentUser {

    private final AppUserRepository appUserRepository;

    public CurrentUser(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public static int getId(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            throw new CurrentUserException("Current user not found in security context authentication.");
        }

        String userIdString = authentication.getName();

        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new CurrentUserException("Current user id in security context authentication principle could not parse to integer.");
        }
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
