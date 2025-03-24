package org.example.fishtank.infrastructure.persistence;

import org.example.fishtank.domain.entity.AppUser;
import org.example.fishtank.domain.entity.Fish;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <AppUser, Integer> {


}
