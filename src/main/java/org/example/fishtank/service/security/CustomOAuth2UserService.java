package org.example.fishtank.service.security;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
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

    AppUserService appUserService;

    public CustomOAuth2UserService(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        var authorizationSite = userRequest.getClientRegistration().getClientName();
        System.out.println("Authorization site: " + authorizationSite);
        var oauth2User = super.loadUser(userRequest);

        if(authorizationSite.equals("GitHub")) {

            var githubUserAttributes = oauth2User.getAttributes();
            var githubId = githubUserAttributes.get("id").toString();
            var githubUsername = githubUserAttributes.get("login").toString();

            if(!appUserService.isNameInDB(githubUsername)) {
                CreateAppUser createAppUser = new CreateAppUser(githubUsername, "password", "No email available.", "Standard");
                appUserService.saveToDB(createAppUser);
            }

            var loginAppUser = appUserService.findByNameForLogin(githubUsername);

            return new OAuth2AppUserPrinciple(loginAppUser);
        }

        throw new OAuth2AuthenticationException(new OAuth2Error("Unauthorized authorizer. Can not authorize users from this site."));
    }
}
