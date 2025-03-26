package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AppUserService {

    UserRepository userRepository;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser createAppUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    public List<AppUser> findAllAppUsers() {
        return userRepository.findAll();
    }

    public AppUser getAppUser(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public Access getAccess(Integer userId) {
        AppUser user = getAppUser(userId);
        return user != null ? user.getAccess() : null;
    }

    public void updateAppUser(Integer id, AppUser updatedUser) {
        AppUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setPasswordHash(updatedUser.getPasswordHash());
        existingUser.setAccess(updatedUser.getAccess());

        userRepository.save(existingUser);
    }

}
