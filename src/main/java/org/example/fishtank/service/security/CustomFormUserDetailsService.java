package org.example.fishtank.service.security;

import org.example.fishtank.model.dto.appUserDto.security.FormAppUserPrinciple;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.service.AppUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomFormUserDetailsService implements UserDetailsService {

    private AppUserService appUserService;

    public CustomFormUserDetailsService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String authenticationCode = "form_" + username;
        System.out.println("loadUserByUsername: authenticationCode: " + authenticationCode);
        LoginAppUser loginAppUser = appUserService.findByAuthenticationCode(authenticationCode);
        System.out.println("loaded from database: \nloginAppUser: " + loginAppUser.name() + "\npassword: " + loginAppUser.password());

        return new FormAppUserPrinciple(loginAppUser);
    }
}
