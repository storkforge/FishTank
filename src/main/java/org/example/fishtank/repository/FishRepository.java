package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Fish;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.List;


@Repository
public interface FishRepository extends ListCrudRepository<Fish, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Fish f SET f.name = :name, f.description = :description WHERE f.id = :id")
    void update(String name, String description, int id);

    @Query("select f from Fish f where f.name = :name")
    Optional<Fish> findByName(String name);

    @Query("SELECT f FROM Fish f WHERE f.appUser.id = :id")
    List<Fish> findByAppUserId(int id);

    @Query("SELECT COUNT(f) FROM Fish f WHERE f.appUser.id = :userId")
    int countByAppUserId(int userId);


}
