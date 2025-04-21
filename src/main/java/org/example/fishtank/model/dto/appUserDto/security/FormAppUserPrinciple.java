package org.example.fishtank.model.dto.appUserDto.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class FormAppUserPrinciple implements UserDetails {

    private final LoginAppUser formLoginAppUser;

    public FormAppUserPrinciple(LoginAppUser formLoginAppUser) {
        this.formLoginAppUser = formLoginAppUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + formLoginAppUser.access()));
    }

    @Override
    public String getPassword() {
        return formLoginAppUser.password();
    }

    @Override
    public String getUsername() {
        return formLoginAppUser.id();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return formLoginAppUser.name();
    }
}
