package org.example.fishtank.model.dto.appUserDto.security;

import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuth2AppUserPrinciple implements OAuth2User {

    private ResponseAppUser responseAppUser;

    public OAuth2AppUserPrinciple(ResponseAppUser responseAppUser) {
        this.responseAppUser = responseAppUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + responseAppUser.access()));
    }

    @Override
    public String getName() {
        return responseAppUser.id().toString();
    }
}
