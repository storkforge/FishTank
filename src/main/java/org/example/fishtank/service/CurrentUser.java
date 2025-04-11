package org.example.fishtank.service;

import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {

    public static int getId(){
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();
        return Integer.parseInt(userIdString);
    }
}
