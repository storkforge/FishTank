package org.example.fishtank.controller;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.service.AppUserService;
import org.example.fishtank.service.CurrentUser;
import org.example.fishtank.service.EventJoiningService;
import org.example.fishtank.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private AppUserService appUserService;

    @MockitoBean
    private EventJoiningService eventJoiningService;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private CurrentUser currentUser;

    @Test
    @WithMockUser
    @DisplayName("GET /event_rough/{id} returns single event in JSON")
    void getEventByIdReturnsEventJson() throws Exception {
        ResponseEvent event = new ResponseEvent(1, "Title", "Text", "City", LocalDateTime.now(), 1);
        when(eventService.findById(1)).thenReturn(event);

        mockMvc.perform(get("/event_rough/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /event_rough returns all events in JSON")
    void getAllEventsReturnsJsonList() throws Exception {
        List<ResponseEvent> events = List.of(new ResponseEvent(1, "Title", "Text", "City", LocalDateTime.now(), 1));
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/event_rough"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.eventList[0].id").value(1));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /add_event returns add event view")
    void showAddEventForm() throws Exception {
        mockMvc.perform(get("/add_event"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_event"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /add_event creates event and redirects")
    void addEventAndRedirect() throws Exception {
        mockMvc.perform(post("/add_event")
                        .param("eventTitle", "Event Title")
                        .param("eventText", "Event Text")
                        .param("cityName", "City")
                        .param("eventDate", LocalDateTime.now().toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(eventService).create(any(CreateEvent.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /update_event/{id} returns update view")
    void showUpdateEventForm() throws Exception {
        ResponseEvent event = new ResponseEvent(1, "Title", "Text", "City", LocalDateTime.now(), 1);
        when(eventService.findById(1)).thenReturn(event);

        mockMvc.perform(get("/update_event/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_event"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attribute("event", event));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /update_event/{id} updates event and redirects")
    void updateEventAndRedirect() throws Exception {
        mockMvc.perform(post("/update_event/1")
                        .param("title", "Updated Title")
                        .param("text", "Updated Text")
                        .param("cityName", "Updated City")
                        .param("eventDate", LocalDateTime.now().toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(eventService).update(eq(1), any(UpdateEvent.class));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /delete_event/{id} deletes event and redirects")
    void deleteEvent() throws Exception {
        mockMvc.perform(post("/delete_event/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(eventService).delete(1);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /events returns event list")
    void showEventsReturnsView() throws Exception {
        ResponseEvent event = new ResponseEvent(1, "Title", "Text", "City", LocalDateTime.now().plusDays(1), 1);
        AppUser user = new AppUser();
        user.setId(1);
        user.setName("Tester");

        when(eventService.getAllEvents()).thenReturn(List.of(event));
        when(appUserRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events"))
                .andExpect(model().attributeExists("eventList", "eventCreators", "reminders"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /my_events returns my events")
    void showMyEventsReturnsView() throws Exception {
        List<ResponseEvent> events = List.of(new ResponseEvent(1, "Title", "Text", "City", LocalDateTime.now(), 1));
        when(eventService.getAllMyEvents()).thenReturn(events);

        mockMvc.perform(get("/my_events"))
                .andExpect(status().isOk())
                .andExpect(view().name("my_events"))
                .andExpect(model().attribute("eventList", events));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /event/{id} shows event details")
    void showEventDetails() throws Exception {
        ResponseEvent event = new ResponseEvent(1, "Title", "Text", "City", LocalDateTime.now(), 1);
        AppUser user = new AppUser();
        user.setId(1);
        user.setName("Tester");

        when(eventService.findById(1)).thenReturn(event);
        when(eventJoiningService.getAllUsersByEventId(1)).thenReturn(List.of());
        when(appUserRepository.findById(1)).thenReturn(Optional.of(user));
        when(currentUser.getUsername()).thenReturn("Tester");
        when(appUserRepository.findByName("Tester")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("event"))
                .andExpect(model().attributeExists("event", "eventCreator", "participants", "isOwner"));
    }
}
