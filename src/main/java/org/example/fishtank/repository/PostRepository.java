package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends ListCrudRepository<Post, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.posttext = :text  WHERE p.id = :id")
    void update(String text, int id);

    @Query("SELECT p FROM Post p WHERE p.fishid.id = :fishId")
    List<Post> findByFishId(Integer fishId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p = :post")
    void delete(@Param("post") Post post);
}
