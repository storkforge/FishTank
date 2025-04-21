package org.example.fishtank.controller;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEventList;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.model.entity.AppUser;
import org.example.fishtank.repository.AppUserRepository;
import org.example.fishtank.service.AppUserService;
import org.example.fishtank.service.CurrentUser;
import org.example.fishtank.service.EventJoiningService;
import org.example.fishtank.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Controller
public class EventController {

    private final EventService eventService;
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    private final EventJoiningService eventJoiningService;
    private final CurrentUser currentUser;

    Logger logger = Logger.getLogger(EventController.class.getName());

    public EventController(EventService eventService, AppUserService appUserService, AppUserRepository appUserRepository, EventJoiningService eventJoiningService, CurrentUser currentUser) {
        this.eventService = eventService;
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
        this.eventJoiningService = eventJoiningService;
        this.currentUser = currentUser;
    }

    @ResponseBody
    @GetMapping(value = "/event_rough/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEvent getEventById(@PathVariable Integer id) {
        return eventService.findById(id);
    }

    @ResponseBody
    @GetMapping(value = "/event_rough", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEventList getEvents() {
        return new ResponseEventList(eventService.getAllEvents());
    }

    @GetMapping("/add_event")
    public String showAddEventForm(Model model) {
        model.addAttribute("event", new CreateEvent("temp", "temp", "temp", LocalDateTime.now()));
        return "add_event";
    }

    @PostMapping("/add_event")
    public String addEvent(
            @RequestParam("eventTitle") String title,
            @RequestParam("eventText") String text,
            @RequestParam("cityName") String cityName,
            @RequestParam("eventDate") LocalDateTime eventDate
    ) {
        CreateEvent createEvent = new CreateEvent(title, text, cityName, eventDate);
        eventService.create(createEvent);
        return "redirect:/events";
    }

    @GetMapping("/update_event/{id}")
    public String showUpdateEventForm(@PathVariable Integer id, Model model) {
        ResponseEvent event = eventService.findById(id);
        model.addAttribute("event", event);
        return "update_event";
    }

    @PostMapping("/update_event/{id}")
    public String updateEvent(
            @PathVariable Integer id,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam(value = "cityName", required = false) String cityName,
            @RequestParam(value = "eventDate", required = false) LocalDateTime eventDate) {

        if (eventDate == null) {
            eventDate = LocalDateTime.now();
        }

        eventService.update(id, new UpdateEvent(title, text, cityName, eventDate));
        return "redirect:/events";
    }


    @PostMapping("/delete_event/{id}")
    public String deleteEvent(@PathVariable Integer id) {
        eventService.delete(id);
        return "redirect:/events";
    }

    @GetMapping("/events")
    public String showEvents(Model model) {
        List<ResponseEvent> eventList = eventService.getAllEvents();
        Map<Integer, AppUser> eventCreators = new HashMap<>();

        for (ResponseEvent event : eventList) {
            appUserRepository.findById(event.appUserId())
                    .ifPresent(user -> eventCreators.put(event.id(), user));
        }

        LocalDateTime from = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0);
        LocalDateTime to = from.withHour(23).withMinute(59);

        List<ResponseEvent> upcomingReminders = eventService.getAllEvents().stream()
                .filter(event -> event.eventDate().isAfter(from) && event.eventDate().isBefore(to))
                .toList();

        model.addAttribute("eventCreators", eventCreators);
        model.addAttribute("eventList", eventList);
        model.addAttribute("reminders", upcomingReminders);


        return "events";
    }

    @GetMapping("/my_events")
    String showMyEvents(Model model) {
        var eventList = eventService.getAllMyEvents();
        model.addAttribute("eventList", eventList);
        return "my_events";
    }

    @GetMapping("/event/{id}")
    public String showEventDetails(@PathVariable Integer id, Model model) {
        ResponseEvent event = eventService.findById(id);

        List<AppUser> participants = eventJoiningService.getAllUsersByEventId(id);

        AppUser eventCreator = appUserRepository.findById(event.appUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String currentAppUser = currentUser.getUsername();

        AppUser currentUser = appUserRepository.findByName(currentAppUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = event.appUserId().equals(currentUser.getId());

        model.addAttribute("event", event);
        model.addAttribute("eventCreator", eventCreator);
        model.addAttribute("participants", participants);
        model.addAttribute("isOwner", isOwner);

        return "event";
    }

    @PostMapping("/join_event/{eventId}")
    public String joinEvent(@PathVariable Integer eventId) {
        try {
            Integer userId = CurrentUser.getId();

            boolean hasJoined = eventJoiningService.hasAppUserJoinedEvent(eventId, userId);

            if (!hasJoined) {
                eventService.joinEvent(eventId, userId);
            }

            return "redirect:/event/" + eventId;

        } catch (Exception e) {
            logger.warning("Error joining event: " + e.getMessage());
            return "redirect:/event/" + eventId;
        }
    }


}