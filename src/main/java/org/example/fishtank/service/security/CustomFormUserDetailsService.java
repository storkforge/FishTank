package org.example.fishtank.service.security;

import org.example.fishtank.model.dto.appUserDto.security.FormAppUserPrinciple;
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

        var loginAppUser = appUserService.findByNameForLogin(username);

        return new FormAppUserPrinciple(loginAppUser);
    }
}
