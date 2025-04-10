package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Access;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface AccessRepository extends ListCrudRepository<Access, Integer> {

    Optional<Access> findByName(String name);
}
