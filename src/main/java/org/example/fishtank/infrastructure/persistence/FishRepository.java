package org.example.fishtank.infrastructure.persistence;

import org.example.fishtank.domain.entity.Fish;
import org.springframework.data.repository.ListCrudRepository;

public interface FishRepository extends ListCrudRepository<Fish, Integer> {
}
