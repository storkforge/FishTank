package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AccessRepository;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.model.mapper.AppUserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AppUserService {
    
    private AppUserRepository appUserRepository;
    private AccessRepository accessRepository;
    private PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, AccessRepository accessRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.accessRepository = accessRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveToDB(CreateAppUser createAppUser) {

        if(!isAuthenticationCodePresentInDB(createAppUser.authenticationCode())) {

            Access access = accessRepository.findByName(createAppUser.access())
                    .orElseThrow(() -> new RuntimeException("Access not found"));

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

    public LoginAppUser findByAuthenticationCode(String authenticationCode) {
        return appUserRepository.findByAuthenticationCode(authenticationCode)
                .map(AppUserMapper::mapToLoginAppUser)
                .orElseThrow(() -> new UsernameNotFoundException("findByAuthenticationCode: User with authentication code: " + authenticationCode + " not found"));
    }

    public LoginAppUser findByNameForLogin(String name) {

        return appUserRepository.findByName(name)
                .map(AppUserMapper::mapToLoginAppUser)
                .orElseThrow(() -> new UsernameNotFoundException("findByNameForLogin: User not found in the database."));
    }

    public ResponseAppUser findByName(String name) {
        return appUserRepository.findByName(name)
                .map(AppUserMapper::mapToResponseAppUser)
                .orElseThrow(() -> new RuntimeException("User not found in the database."));
    }

    public ResponseAppUser findById(Integer id) {
        return appUserRepository.findById(id)
                .map(AppUserMapper::mapToResponseAppUser)
                .orElseThrow(() -> new RuntimeException("User not found in the database."));
    }

    public List<ResponseAppUser> findAllAppUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(AppUserMapper::mapToResponseAppUser)
                .filter(Objects::nonNull)
                .toList();
    }

    public Access getAccess(Integer id) {
        return appUserRepository.findById(id)
                .map(AppUser::getAccess)
                .orElse(null);
    }

    public void update(int id, UpdateAppUser updateAppUser) {

        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found in the database."));

        if(updateAppUser.name() != null) {
            appUser.setName(updateAppUser.name());
        }

        if(updateAppUser.access() != null) {
            Access updateAccess = accessRepository.findByName(updateAppUser.access())
                    .orElseThrow(() -> new RuntimeException("Access not found"));

            appUser.setAccess(updateAccess);
        }

        if(updateAppUser.passwordHash() != null) {
            String passwordHash = passwordEncoder.encode(updateAppUser.passwordHash());
            appUser.setPasswordHash(passwordHash);
        }

        if(updateAppUser.email() != null) {
            appUser.setEmail(updateAppUser.email());
        }
    }

    public boolean isNamePresentInDB(String name) {
        return appUserRepository.findByName(name).isPresent();
    }

    public boolean isAuthenticationCodePresentInDB(String authenticationCode) {
        return appUserRepository.findByAuthenticationCode(authenticationCode).isPresent();
    }
}


