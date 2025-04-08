package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AccessRepository;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.model.mapper.AppUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.fishtank.model.mapper.AppUserMapper.map;

@Service
@Transactional
public class AppUserService {
    
    private AppUserRepository appUserRepository;
    private AccessRepository accessRepository;

    public AppUserService(AppUserRepository appUserRepository, AccessRepository accessRepository) {
        this.appUserRepository = appUserRepository;
        this.accessRepository = accessRepository;
    }

    public void saveToDBIfNameNotFound(CreateAppUser createAppUser) {

        if(appUserRepository.findByName(createAppUser.name()).isEmpty()) {

            System.out.println("User was not found in the database.");

            Access access = accessRepository.findByName(createAppUser.access());
            var appUserToSave = map(createAppUser, access);
            appUserRepository.save(appUserToSave);
        }

        System.out.println("User was found in the database.");
    }

    public ResponseAppUser findByName(String appUserName) {
        return appUserRepository.findByName(appUserName)
                .map(AppUserMapper::map)
                .orElseThrow(() -> new RuntimeException("User not found in the database."));
    }

    public ResponseAppUser findById(Integer id) {
        return appUserRepository.findById(id)
                .map(AppUserMapper::map)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<ResponseAppUser> findAllAppUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(AppUserMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    public Access getAccess(Integer id) {
        return appUserRepository.findById(id)
                .map(AppUser::getAccess)
                .orElse(null);
    }

//    public void update(int id, UpdateAppUser user) {
//        AppUser existingUser = userRepository.findById(id).orElseThrow(() ->
//                new RuntimeException("User not found"));
//        map(user, existingUser);
//        userRepository.update(existingUser.getName(),existingUser.getPasswordHash(), existingUser.getEmail(), existingUser.getAccess().toString());
//    }
}


