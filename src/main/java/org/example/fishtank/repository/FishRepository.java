package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Fish;
import org.springframework.data.repository.ListCrudRepository;



public interface FishRepository extends ListCrudRepository<Fish, Integer> {


    void update(Fish oldFish);
}
