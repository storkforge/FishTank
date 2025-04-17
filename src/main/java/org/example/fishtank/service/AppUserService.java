package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.exception.message.Message;
import org.example.fishtank.exception.custom.ResourceNotFoundException;
import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AccessRepository;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.model.mapper.AppUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AppUserService {

    public final static String ACCESS_PREMIUM = "Premium";
    public final static String ACCESS_STANDARD = "Standard";

    private final AppUserRepository appUserRepository;
    private final AccessRepository accessRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, AccessRepository accessRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.accessRepository = accessRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginAppUser getLoginAppUserByAuthenticationCode(String authenticationCode) {
        return appUserRepository.findByAuthenticationCode(authenticationCode)
                .map(AppUserMapper::mapToLoginAppUser)
                .orElseThrow(() -> new ResourceNotFoundException(Message.APP_USER.notFound()));
    }

    public ResponseAppUser getResponseAppUserById(Integer id) {
        return appUserRepository.findById(id)
                .map(AppUserMapper::mapToResponseAppUser)
                .orElseThrow(() -> new ResourceNotFoundException(Message.APP_USER.notFound()));
    }

    public List<ResponseAppUser> getAllAppUsersAsResponseList() {
        return appUserRepository.findAll()
                .stream()
                .map(AppUserMapper::mapToResponseAppUser)
                .filter(Objects::nonNull)
                .toList();
    }

    public void save(CreateAppUser createAppUser) {

        if(!isAppUserAuthenticationCodePresentInDB(createAppUser.authenticationCode())) {

            Access access = getAccessByName(createAppUser.access());

            String passwordHash = passwordEncoder.encode(createAppUser.password());

            AppUser appUser = new AppUser();
            appUser.setName(createAppUser.name());
            appUser.setPasswordHash(passwordHash);
            appUser.setEmail(createAppUser.email());
            appUser.setAuthenticationCode(createAppUser.authenticationCode());
            appUser.setAccess(access);

            appUserRepository.save(appUser);
        }
    }

    public void updateAppUser(Integer appUserId, UpdateAppUser updateAppUser) {

        AppUser appUserToUpdate = getAppUserById(appUserId);

        if(updateAppUser.name() != null) {
            appUserToUpdate.setName(updateAppUser.name());
        }

        if(updateAppUser.email() != null) {
            appUserToUpdate.setEmail(updateAppUser.email());
        }

        if(updateAppUser.password() != null) {
            appUserToUpdate.setPasswordHash(passwordEncoder.encode(updateAppUser.password()));
        }
    }

    public void updateAppUserToAccessStandard(Integer appUserId) {

        AppUser appUserToUpdate = getAppUserById(appUserId);
        Access accessNew = getAccessByName(ACCESS_STANDARD);

        appUserToUpdate.setAccess(accessNew);
    }

    public void updateAppUserToAccessPremium(Integer appUserId) {

        AppUser appUserToUpdate = getAppUserById(appUserId);
        Access accessNew = getAccessByName(ACCESS_PREMIUM);

        appUserToUpdate.setAccess(accessNew);
    }

    public boolean isAppUserAuthenticationCodePresentInDB(String authenticationCode) {
        return appUserRepository.findByAuthenticationCode(authenticationCode).isPresent();
    }

    // PRIVATE METHODS TO KEEP ENTITIES WITHIN SERVICE CLASS

    private AppUser getAppUserById(int id){
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Message.APP_USER.notFound()));
    }

    private Access getAccessByName(String name) {
        return accessRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(Message.ACCESS.notFound()));
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(appUser.getName())
                .password(appUser.getPasswordHash())
                .authorities("ROLE_" + appUser.getAccess().getName()) // e.g., ROLE_Premium
                .build();
    }
}


