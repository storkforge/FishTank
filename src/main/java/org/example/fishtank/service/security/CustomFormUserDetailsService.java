package org.example.fishtank.service.security;

import org.example.fishtank.model.dto.appUserDto.security.FormAppUserPrinciple;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomFormUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    public CustomFormUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = appUserRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new FormAppUserPrinciple(appUser);
    }
}
