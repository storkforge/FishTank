package org.example.fishtank.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.fishtank.model.entity.Post;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.posttext = :text  WHERE p.id = :id")
    void update(String text, int id);

    @Query("SELECT p FROM Post p WHERE p.fishid.id = :fishId")
    List<Post> findByFishId(Integer fishId);
    List<Post> findByFishId(int fishId);

    @Query(value = """
    SELECT * FROM post 
    WHERE ST_DWithin(coordinate, :point, :distance) 
    ORDER BY ST_Distance(coordinate, :point)
    """, nativeQuery = true)
    List<Post> findNearbyPosts(@Param("point") Point point, @Param("distance") double distance);

}
