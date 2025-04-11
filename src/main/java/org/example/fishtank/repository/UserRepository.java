/*
package org.example.fishtank.repository;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.entity.Access;
import org.example.fishtank.model.entity.AppUser;

import org.example.fishtank.model.entity.WaterType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface UserRepository extends ListCrudRepository<AppUser, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.name = :name, u.passwordHash =:passwordHash, u.email = :email, u.access = :access WHERE u.id = :id")
    void update(@Param("name") String name, @Param("passwordHash") String passwordHash, @Param("email") String email, @Param("access") Access access);

}
*/
