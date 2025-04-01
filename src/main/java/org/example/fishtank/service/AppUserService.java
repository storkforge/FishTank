package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.CreateAppUser;
import org.example.fishtank.model.dto.ResponsAppUser;
import org.example.fishtank.model.dto.UpdateAppUser;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.UserRepository;
import org.example.fishtank.model.mapper.AppUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static org.example.fishtank.model.mapper.AppUserMapper.map;

@Service
@Transactional
public class AppUserService {

    UserRepository userRepository;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void save(CreateAppUser user) {
        var newUser = map(user);
        userRepository.save(newUser);
    }



    public ResponsAppUser findById(Integer id) {
        return userRepository.findById(id)
                .map(AppUserMapper::map)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<ResponsAppUser> findAllAppUsers() {
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

    public void update(int id, UpdateAppUser user) {
        AppUser existingUser = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        map(user, existingUser);
        userRepository.update(existingUser.getName(),existingUser.getPasswordHash(), existingUser.getEmail(), existingUser.getAccess().toString());
    }
}


