package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends ListCrudRepository<Post, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.posttext = :text  WHERE p.id = :id")
    void update(String text, int id);
}
