package org.example.fishtank.model.dto.appUserDto.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuth2AppUserPrinciple implements OAuth2User {

    private final LoginAppUser loginAppUser;

    public OAuth2AppUserPrinciple(LoginAppUser loginAppUser) {
        this.loginAppUser = loginAppUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("id", loginAppUser.id(),"name", loginAppUser.name(), "email", loginAppUser.email());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + loginAppUser.access()));
    }

    @Override
    public String getName() {
        return loginAppUser.id();
    }
}
