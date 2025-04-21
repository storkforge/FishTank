package org.example.fishtank.service;

import jakarta.transaction.Transactional;
import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Event;
import org.example.fishtank.model.entity.EventJoining;
import org.example.fishtank.model.mapper.EventMapper;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.repository.EventJoiningRepository;
import org.example.fishtank.repository.EventRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final AppUserRepository appUserRepository;
    private final EventJoiningRepository eventJoiningRepository;

    public EventService(EventRepository eventRepository, AppUserRepository appUserRepository, EventJoiningRepository eventJoiningRepository) {
        this.eventRepository = eventRepository;
        this.appUserRepository = appUserRepository;
        this.eventJoiningRepository = eventJoiningRepository;
    }

    @Cacheable(value = "event", key = "#id")
    public ResponseEvent findById(Integer id) {
        return eventRepository.findById(id)
                .map(EventMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Cacheable("allEvents")
    public List<ResponseEvent> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    @CacheEvict(value = {"event", "allEvents", "myEvents"}, allEntries = true)
    public void create(CreateEvent createEvent) {
        var appUser = appUserRepository.findById(CurrentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = EventMapper.map(createEvent, appUser);
        assert event != null;
        eventRepository.save(event);
    }

    @CacheEvict(value = {"event", "allEvents", "myEvents"}, key = "#id", allEntries = true)
    public void update(Integer id, UpdateEvent updateEvent) {
        Event oldEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (!oldEvent.getAppUserId().getId().equals(CurrentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own event");
        }

        EventMapper.map(updateEvent, oldEvent);
        eventRepository.update(oldEvent.getEventtext(), oldEvent.getId());
    }

    @CacheEvict(value = {"event", "allEvents", "myEvents"}, key = "#id", allEntries = true)
    public void delete(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (!event.getAppUserId().getId().equals(CurrentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own event");
        }

        eventRepository.delete(event);
    }

    public List<ResponseEvent> getAllMyEvents() {
        Integer currentUserId = CurrentUser.getId();

        return eventRepository.findByAppUserId(currentUserId).stream()
                .map(EventMapper::map)
                .filter(Objects::nonNull)
                .toList();
    }

    public void joinEvent(Integer eventId, Integer userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EventJoining eventJoining = new EventJoining();
        eventJoining.setEventId(event);
        eventJoining.setAppUserId(appUser);

        eventJoiningRepository.save(eventJoining);
    }

    public Object getEventById(Integer id) {
        return eventRepository.findById(id)
                .map(EventMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    public List<Event> findEventsByDate(LocalDate date) {
        return eventRepository.findEventByDateBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }
}
