package org.example.fishtank.model.dto.appUserDto.security;

import org.example.fishtank.model.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class FormAppUserPrinciple implements UserDetails {

    private AppUser appUser;

    public FormAppUserPrinciple(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getAccess().getName()));
    }

    @Override
    public String getPassword() {
        return appUser.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return appUser.getName();
    }
}
