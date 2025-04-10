package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
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
        appUser.setEmail(createAppUser.email());
        appUser.setPasswordHash(passwordHash);
        appUser.setAccess(access);
        return appUser;
    }

    public static LoginAppUser mapToLoginAppUser(AppUser appUser) {

        if(null == appUser){
            return null;
        }

        return new LoginAppUser(
                appUser.getId().toString(),
                appUser.getName(),
                appUser.getPasswordHash(),
                appUser.getPasswordHash(),
                appUser.getAccess().getName()
        );
    }
}
