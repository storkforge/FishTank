package org.example.fishtank.repository;

import org.example.fishtank.model.entity.WaterType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface WaterTypeRepository extends ListCrudRepository<WaterType, Integer> {

    @Query("select w from WaterType w where w.name = :name")
    WaterType findByName(String name);
}
