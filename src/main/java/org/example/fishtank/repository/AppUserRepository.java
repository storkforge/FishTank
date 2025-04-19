package org.example.fishtank.repository;

import org.example.fishtank.model.entity.AppUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends ListCrudRepository<AppUser, Integer> {

    Optional<AppUser> findByAuthenticationCode(String authenticationCode);

    Optional<AppUser> findByName(String name);
}
