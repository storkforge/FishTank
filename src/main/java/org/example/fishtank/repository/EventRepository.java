package org.example.fishtank.repository;

import org.example.fishtank.model.entity.Event;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends ListCrudRepository<Event, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.eventText = :text  WHERE e.id = :id")
    void update(String text, int id);

    @Query("SELECT e FROM Event e WHERE e.appUserId.id = :appUserId")
    List<Event> findByAppUserId(Integer appUserId);

    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate")
    List<Event> findEventByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM Event e WHERE e.eventDate < :now")
    void deletePassedEvents(LocalDateTime now);


}
