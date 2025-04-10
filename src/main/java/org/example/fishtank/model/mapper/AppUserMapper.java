package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.security.LoginAppUser;
import org.example.fishtank.model.entity.*;

public class AppUserMapper {

    public static ResponseAppUser mapToResponseAppUser(AppUser appUser) {
        if (null == appUser)
            return null;
        return new ResponseAppUser(
                appUser.getId(),
                appUser.getName(),
                appUser.getAccess().getName());
    }

    public static AppUser mapToAppUser(CreateAppUser createAppUser, Access access, String passwordHash) {

        if (null == createAppUser || null == access || passwordHash == null)
            return null;

        AppUser appUser = new AppUser();
        appUser.setName(createAppUser.name());
        appUser.setPasswordHash(passwordHash);
        appUser.setEmail(createAppUser.email());
        appUser.setAuthenticationCode(createAppUser.authenticationCode());
        appUser.setAccess(access);
        return appUser;
    }

    public static LoginAppUser mapToLoginAppUser(AppUser appUser) {

        if(null == appUser){
            return null;
        }

        String id = appUser.getId().toString();
        String name = appUser.getName();
        String password = appUser.getPasswordHash();
        String email = appUser.getEmail();
        String authenticationCode = appUser.getAuthenticationCode();
        String access = appUser.getAccess().getName();

        return new LoginAppUser(id, name, password, email, authenticationCode, access);
    }
}
