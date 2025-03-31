package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Access;
import org.springframework.data.repository.ListCrudRepository;

public interface AccessRepository extends ListCrudRepository<Access, Integer> {
    Access findByName(String name);
}
