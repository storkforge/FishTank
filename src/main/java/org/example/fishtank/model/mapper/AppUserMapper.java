package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.CreateAppUser;
import org.example.fishtank.model.dto.UpdateAppUser;
import org.example.fishtank.model.dto.ResponsAppUser;
import org.example.fishtank.model.entity.*;
import org.example.fishtank.repository.AccessRepository;


public class AppUserMapper {
    private AppUserMapper() {}

    static AccessRepository accessRepository;

    public static ResponsAppUser map(AppUser appUser) {
        if (null == appUser)
            return null;
        return new ResponsAppUser(
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
