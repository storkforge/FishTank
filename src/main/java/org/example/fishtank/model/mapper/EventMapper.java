package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Event;

import java.util.Objects;

public class EventMapper {

    public static ResponseEvent map(Event event) {
        if (event == null) {
            return null;
        }
        return new ResponseEvent(
                event.getId(),
                event.getEventTitle(),
                event.getEventtext(),
                event.getCityName(),
                event.getEventDate(),
                event.getAppUserId() != null ? event.getAppUserId().getId() : null
        );
    }

    public static Event map(CreateEvent createEvent, AppUser appUser) {
        if (Objects.isNull(createEvent))
            return null;
        Event event = new Event();
        event.setEventTitle(createEvent.title());
        event.setEventtext(createEvent.text());
        event.setAppUserId(appUser);
        event.setCityName(createEvent.cityName());
        event.setEventDate(createEvent.eventDate());
        return event;
    }

    public static void map(UpdateEvent updateEvent, Event oldEvent) {
        if (Objects.nonNull(updateEvent.text())) {
            oldEvent.setEventtext(updateEvent.text());
        }
        if (updateEvent.cityName() != null){
            oldEvent.setCityName(updateEvent.cityName());
        }
        if (updateEvent.eventDate() != null){
            oldEvent.setEventDate(updateEvent.eventDate());
        }
    }

}
