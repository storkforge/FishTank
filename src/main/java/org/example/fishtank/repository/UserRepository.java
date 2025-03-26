package org.example.fishtank.repository;

import org.example.fishtank.model.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <AppUser, Integer> {
}
