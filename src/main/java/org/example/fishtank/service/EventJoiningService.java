package org.example.fishtank.service;

import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Event;
import org.example.fishtank.model.entity.EventJoining;
import org.example.fishtank.repository.EventJoiningRepository;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventJoiningService {

    private final EventJoiningRepository eventJoiningRepository;
    private final AppUserRepository appUserRepository;
    private final EventRepository eventRepository;

    public EventJoiningService(EventJoiningRepository eventJoiningRepository,
                               AppUserRepository appUserRepository,
                               EventRepository eventRepository) {
        this.eventJoiningRepository = eventJoiningRepository;
        this.appUserRepository = appUserRepository;
        this.eventRepository = eventRepository;
    }

    public void joinEvent(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyJoined = eventJoiningRepository.findByEventId(eventId).stream()
                .anyMatch(ej -> ej.getAppUserId().getId().equals(userId));
        if (alreadyJoined) return;

        EventJoining joining = new EventJoining();
        joining.setEventId(event);
        joining.setAppUserId(user);
        eventJoiningRepository.save(joining);
    }

    public List<AppUser> getAllUsersByEventId(Integer eventId) {
        return eventJoiningRepository.findByEventId(eventId).stream()
                .map(EventJoining::getAppUserId)
                .toList();
    }

    public boolean hasAppUserJoinedEvent(Integer eventId, Integer userId) {
        return eventJoiningRepository.findByEventId(eventId).stream()
                .anyMatch(ej -> ej.getAppUserId().getId().equals(userId));
    }
}
