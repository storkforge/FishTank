package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.repository.AccessRepository;


public class AppUserMapper {
    private AppUserMapper() {}

    static AccessRepository accessRepository;

    public static ResponseAppUser map(AppUser appUser) {
        if (null == appUser)
            return null;
        return new ResponseAppUser(
                appUser.getId(),
                appUser.getName(),
                appUser.getAccess().toString());
    }

    public static AppUser map(CreateAppUser createAppUser) {
        if (null == createAppUser)
            return null;
        AppUser appUser = new AppUser();
        appUser.setName(createAppUser.name());
        appUser.setAccess(accessRepository.findByName(createAppUser.access()));
        return appUser;
    }


    public static void map(UpdateAppUser updateAppUser, AppUser appUser) {
        if (updateAppUser.name() != null) {
            appUser.setName(updateAppUser.name());
        }
        if (updateAppUser.access() != null) {
            appUser.setAccess(accessRepository.findByName(updateAppUser.access()));
        }
    }
}
