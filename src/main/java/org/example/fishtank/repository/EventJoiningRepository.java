package org.example.fishtank.repository;

import org.example.fishtank.model.entity.EventJoining;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface EventJoiningRepository extends ListCrudRepository<EventJoining, Integer> {

    @Query("SELECT j FROM EventJoining j WHERE j.eventId.id = :eventId")
    List<EventJoining> findByEventId(Integer eventId);

}
