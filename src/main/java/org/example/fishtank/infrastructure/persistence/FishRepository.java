package org.example.fishtank.infrastructure.persistence;

import org.example.fishtank.domain.entity.Fish;
import org.springframework.data.repository.CrudRepository;

public interface FishRepository extends CrudRepository <Fish, Integer> {
}
