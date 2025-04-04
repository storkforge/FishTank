package org.example.fishtank.model.dto.appUserDto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuth2AppUserPrinciple implements OAuth2User {

    OAuth2User oAuth2User;

    public OAuth2AppUserPrinciple(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getAccess().getName()));
        return List.of();
    }
}
