package org.example.fishtank.model.mapper;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    LocalDateTime localDateTime;
    AppUser appUser;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.of(2000, 10, 10, 10, 10);

        appUser = new AppUser();
        appUser.setId(1);
    }

    @Test
    void mapEventToResponseEventShouldReturnSame() {
        Integer expectedId = 1;
        String expectedEventTitle = "Test Title";
        String expectedEventText = "This is a test event";
        String expectedCityName = "Gothenburg";
        LocalDateTime expectedLocalDateTime = localDateTime;
        Integer expectedAppUserId = 1;

        Event testEvent = new Event();
        testEvent.setId(expectedId);
        testEvent.setEventTitle(expectedEventTitle);
        testEvent.setEventtext(expectedEventText);
        testEvent.setCityName(expectedCityName);
        testEvent.setEventDate(localDateTime);
        testEvent.setAppUserId(appUser);

        ResponseEvent result = EventMapper.map(testEvent);

        assertAll(
                () -> assertThat(result.id()).isEqualTo(expectedId),
                () -> assertThat(result.title()).isEqualTo(expectedEventTitle),
                () -> assertThat(result.text()).isEqualTo(expectedEventText),
                () -> assertThat(result.cityName()).isEqualTo(expectedCityName),
                () -> assertThat(result.eventDate()).isEqualTo(expectedLocalDateTime),
                () -> assertThat(result.appUserId()).isEqualTo(expectedAppUserId)
        );
    }

    @Test
    void mapNullEventToResponseEvent() {
        ResponseEvent responseEvent = EventMapper.map(null);
        assertNull(responseEvent);
    }

    @Test
    void mapCreateEventToEventShouldReturnSame() {
        String expectedEventTitle = "New title";
        String expectedEventText = "This is a new event";
        String expectedCityName = "New city";
        LocalDateTime expectedLocalDateTime = localDateTime;
        Integer expectedAppUserId = 1;

        CreateEvent createEvent = new CreateEvent(
                expectedEventTitle,
                expectedEventText,
                expectedCityName,
                localDateTime
        );

        Event result = EventMapper.map(createEvent, appUser);

        assertAll(
                () -> assertThat(result.getEventTitle()).isEqualTo(expectedEventTitle),
                () -> assertThat(result.getEventtext()).isEqualTo(expectedEventText),
                () -> assertThat(result.getCityName()).isEqualTo(expectedCityName),
                () -> assertThat(result.getEventDate()).isEqualTo(expectedLocalDateTime),
                () -> assertThat(result.getAppUserId().getId()).isEqualTo(expectedAppUserId)
        );
    }

    @Test
    void mapNullCreateEventToEvent() {
        Event event = EventMapper.map(null, appUser);
        assertNull(event);
    }

    @Test
    void mapUpdateEventToEventShouldOnlyUpdateChangedField() {
        String updatedEventTitle = "Updated title";
        String updatedEventText = "This is a updated event";
        LocalDateTime updatedLocalDateTime = LocalDateTime.of(2010, 10, 10, 10, 10);

        UpdateEvent updateEvent = new UpdateEvent(
                updatedEventTitle,
                updatedEventText,
                null,
                updatedLocalDateTime
        );

        Event oldEvent = new Event();
        oldEvent.setId(2);
        oldEvent.setEventTitle("Old title");
        oldEvent.setEventtext("Old text");
        oldEvent.setCityName("Old city");
        oldEvent.setEventDate(localDateTime);
        oldEvent.setAppUserId(appUser);

        EventMapper.map(updateEvent, oldEvent);

        assertAll(
                () -> assertThat(oldEvent.getEventTitle()).isEqualTo(oldEvent.getEventTitle()),
                () -> assertThat(oldEvent.getEventtext()).isEqualTo(updatedEventText),
                () -> assertThat(oldEvent.getCityName()).isEqualTo("Old city"),
                () -> assertThat(oldEvent.getEventDate()).isEqualTo(updatedLocalDateTime)
        );
    }
}