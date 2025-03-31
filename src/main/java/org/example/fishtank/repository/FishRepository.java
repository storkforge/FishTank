package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Fish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FishRepository extends ListCrudRepository<Fish, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Fish f SET f.name = :name, f.description = :description WHERE f.id = :id")
    void update(String name, String description, int id);

}
