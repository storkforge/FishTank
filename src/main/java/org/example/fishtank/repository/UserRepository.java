package org.example.fishtank.repository;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.entity.AppUser;

import org.example.fishtank.model.entity.WaterType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends ListCrudRepository<AppUser, Integer> {

    AppUser findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.name = :name, u.passwordHash =:passwordHash, u.email = :email, u.access = :access WHERE u.id = :id")
    void update(@Param("name") String name, @Param("passwordHash") String passwordHash, @Param("email") String email, @Param("access") String access);

    @Query("select a from AppUser a where a.name = :name")
    AppUser findByName(String name);
}
