package org.example.fishtank.repository;

import org.example.fishtank.model.entity.AppUser;
import org.springframework.data.repository.ListCrudRepository;

public interface PostRepository extends ListCrudRepository<AppUser, Integer> {
}
