package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Sex;
import org.springframework.data.repository.ListCrudRepository;

public interface SexRepository extends ListCrudRepository<Sex, Integer> {
}
