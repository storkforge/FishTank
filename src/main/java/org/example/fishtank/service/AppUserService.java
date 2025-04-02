package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.appUserDto.CreateAppUser;
import org.example.fishtank.model.dto.appUserDto.ResponseAppUser;
import org.example.fishtank.model.dto.appUserDto.UpdateAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AccessRepository;
import org.example.fishtank.repository.UserRepository;
import org.example.fishtank.model.mapper.AppUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.fishtank.model.mapper.AppUserMapper.map;

@Service
@Transactional
public class AppUserService {

    private UserRepository userRepository;
    private AccessRepository accessRepository;

    public AppUserService(UserRepository userRepository, AccessRepository accessRepository) {
        this.userRepository = userRepository;
        this.accessRepository = accessRepository;
    }

    public void save(String oAuth2UserName){
        CreateAppUser createAppUser = new CreateAppUser(oAuth2UserName, "email", "password", "Standard");
        save(createAppUser);
    }

    public void save(CreateAppUser createAppUser) {

        if(userRepository.findByName(createAppUser.name()) == null){
            System.out.println("User is not found");
            Access access = accessRepository.findByName(createAppUser.access());
            var appUser = map(createAppUser, access);
            userRepository.save(appUser);
        }
        System.out.println("User is found");
    }

    public ResponseAppUser findById(Integer id) {
        return userRepository.findById(id)
                .map(AppUserMapper::map)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<ResponseAppUser> findAllAppUsers() {
        return userRepository.findAll()
                .stream()
                .map(AppUserMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    public Access getAccess(Integer id) {
        return userRepository.findById(id)
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


