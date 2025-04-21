package org.example.fishtank.service;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.model.entity.Event;
import org.example.fishtank.model.mapper.EventMapper;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.repository.EventJoiningRepository;
import org.example.fishtank.repository.EventRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private EventJoiningRepository eventJoiningRepository;

    private final Integer userId = 1;
    private Event event;
    private AppUser appUser;

    MockedStatic<CurrentUser> mockedCurrentUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setId(userId);

        event = new Event();
        event.setId(1);
        event.setEventtext("Cool Event");
        event.setEventtext("This is a fun event.");
        event.setCityName("Stockholm");
        event.setEventDate(LocalDateTime.of(2025, 4, 25, 15, 30));
        event.setAppUserId(appUser);

        mockedCurrentUser = mockStatic(CurrentUser.class);
        mockedCurrentUser.when(CurrentUser::getId).thenReturn(userId);
    }

    @AfterEach
    void tearDown() {
        mockedCurrentUser.close();
    }

    @Test
    void findById_ReturnsMappedResponseEvent() {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        ResponseEvent expected = EventMapper.map(event);

        ResponseEvent actual = eventService.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void findById_ThrowsIfNotFound() {
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> eventService.findById(1));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getAllEvents_ReturnsMappedList() {
        when(eventRepository.findAll()).thenReturn(List.of(event));

        List<ResponseEvent> result = eventService.getAllEvents();

        assertEquals(1, result.size());
        assertEquals(EventMapper.map(event), result.get(0));
    }

    @Test
    void create_SavesEvent() {
        CreateEvent createEvent = new CreateEvent(
                "New Event",
                "Details about the event",
                "Gothenburg",
                LocalDateTime.of(2025, 5, 5, 18, 0)
        );

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(appUser));

        eventService.create(createEvent);

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void create_ThrowsIfUserNotFound() {
        CreateEvent createEvent = new CreateEvent(
                "New Event",
                "Description",
                "City",
                LocalDateTime.now()
        );

        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        var ex = assertThrows(ResponseStatusException.class,
                () -> eventService.create(createEvent));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void update_UpdatesEventText() {
        UpdateEvent updateEvent = new UpdateEvent("Updated Title", "Updated text", "NewCity", LocalDateTime.now());

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        eventService.update(1, updateEvent);

        verify(eventRepository, times(1)).update(anyString(), eq(1));
    }

    @Test
    void update_ThrowsIfNotOwner() {
        event.getAppUserId().setId(2); // not the current user
        UpdateEvent updateEvent = new UpdateEvent("Updated", "Text", "City", LocalDateTime.now());

        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        var ex = assertThrows(ResponseStatusException.class,
                () -> eventService.update(1, updateEvent));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void delete_RemovesEvent() {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        eventService.delete(1);

        verify(eventRepository).delete(event);
    }

    @Test
    void delete_ThrowsIfNotOwner() {
        event.getAppUserId().setId(2); // not the current user
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));

        var ex = assertThrows(ResponseStatusException.class, () -> eventService.delete(1));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void getAllMyEvents_ReturnsUserEvents() {
        when(eventRepository.findByAppUserId(userId)).thenReturn(List.of(event));

        List<ResponseEvent> result = eventService.getAllMyEvents();

        assertEquals(1, result.size());
        assertEquals(EventMapper.map(event), result.get(0));
    }

    @Test
    void joinEvent_SavesEventJoining() {
        when(eventRepository.findById(1)).thenReturn(Optional.of(event));
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(appUser));

        eventService.joinEvent(1, userId);

        verify(eventJoiningRepository, times(1)).save(any());
    }

    @Test
    void findEventsByDate_ReturnsList() {
        LocalDate date = LocalDate.of(2025, 4, 22);
        when(eventRepository.findEventByDateBetween(
                date.atStartOfDay(), date.plusDays(1).atStartOfDay()))
                .thenReturn(List.of(event));

        List<Event> result = eventService.findEventsByDate(date);

        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
    }
}
