package org.example.fishtank.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        var oauth2User = super.loadUser(userRequest);
        var attributes = oauth2User.getAttributes();

        // for github login
        if(attributes.containsKey("login")) {
           // authorizedRegistrationClientId getCOnt. getAuth.

        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE"));

        return null;
    }
}
