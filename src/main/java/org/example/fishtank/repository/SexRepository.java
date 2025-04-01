package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Sex;
import org.example.fishtank.model.entity.WaterType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface SexRepository extends ListCrudRepository<Sex, Integer> {

    @Query("select s from Sex s where s.name = :name")
    Sex findByName(String name);
}
