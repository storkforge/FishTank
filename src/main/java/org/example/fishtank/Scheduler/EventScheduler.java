package org.example.fishtank.Scheduler;

import org.example.fishtank.model.entity.Event;
import org.example.fishtank.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventScheduler {

    private final EventRepository eventRepository;

    public EventScheduler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void findEventByDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in7Days = now.plusDays(7);
        List<Event> events = eventRepository.findEventByDateBetween(in7Days.withHour(0).withMinute(0), in7Days.withHour(23).withMinute(59));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUpOldEvents() {
        LocalDateTime now = LocalDateTime.now();
        eventRepository.deletePassedEvents(now);
    }
}


