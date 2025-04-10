package org.example.fishtank.service.security;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.model.dto.appUserDto.security.OAuth2AppUserPrinciple;
import org.example.fishtank.service.AppUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Service class for login with OAuth2.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private AppUserService appUserService;

    public CustomOAuth2UserService(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        String authorizationSite = userRequest.getClientRegistration().getClientName();

        if(authorizationSite.equals("GitHub")) {

            String githubId = oauth2User.getAttributes().get("id").toString();
            String authenticationCode = "git_" + githubId;

            // add git user to DB if not already present.
            if(!appUserService.isAuthenticationCodePresentInDB(authenticationCode)) {

                String githubUsername = oauth2User.getAttributes().get("login").toString();
                CreateAppUser createAppUser = new CreateAppUser(githubUsername, "password", "No email available", authenticationCode,"Standard");
                appUserService.saveToDB(createAppUser);
            }

            LoginAppUser loginAppUser = appUserService.findByAuthenticationCode(authenticationCode);

            return new OAuth2AppUserPrinciple(loginAppUser);
        }

        throw new OAuth2AuthenticationException(new OAuth2Error("Unauthorized authorizer. Can not authorize users from this site."));
    }
}
