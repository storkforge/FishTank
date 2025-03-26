package org.example.fishtank.repository;

import org.example.fishtank.model.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface UserRepository extends ListCrudRepository<AppUser, Integer> {

}
