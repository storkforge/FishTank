package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Event;
import org.example.fishtank.model.entity.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventRepository extends ListCrudRepository<Event, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.eventtext = :text  WHERE e.id = :id")
    void update(String text, int id);

    @Query("SELECT e FROM Event e WHERE e. appUserId.id = :appUserId")
    List<Post> findByAppUserId(Integer appUserId);
}
