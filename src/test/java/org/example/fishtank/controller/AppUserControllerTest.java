package org.example.fishtank.controller;

import org.example.fishtank.service.CurrentUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppUserController.class)
class AppUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CurrentUser currentUser;

    private OAuth2AuthenticationToken getMockOAuthToken() {
        OAuth2User oAuth2User = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_Premium")),
                Map.of("login", "TestUser"),
                "login"
        );

        return new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), "github");
    }

    @Test
    void home_ShouldReturnHomeViewWithUsername() throws Exception {
        when(currentUser.getUsername()).thenReturn("TestUser");

        mockMvc.perform(get("/home").with(authentication(getMockOAuthToken())))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("username", "TestUser"));
    }

    @Test
    void profile_ShouldReturnProfileViewWithAttributes() throws Exception {
        when(currentUser.getUsername()).thenReturn("TestUser");
        when(currentUser.getRole()).thenReturn("ROLE_Premium");

        mockMvc.perform(get("/profile").with(authentication(getMockOAuthToken())))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", "TestUser"))
                .andExpect(model().attribute("authentication", "ROLE_Premium"));
    }
}