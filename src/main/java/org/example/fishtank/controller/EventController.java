package org.example.fishtank.controller;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEventList;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.service.AppUserService;
import org.example.fishtank.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class EventController {

    private final EventService eventService;
    private final AppUserService appUserService;

    public EventController(EventService eventService, AppUserService appUserService) {
        this.eventService = eventService;
        this.appUserService = appUserService;

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
        // Fetch the event list from the service
        var eventList = eventService.getAllEvents();

        // Ensure eventList is not null and properly populated
        if (eventList != null) {
            model.addAttribute("eventList", eventList);
        } else {
            model.addAttribute("eventList", new ArrayList<>()); // Avoid null pointer issues
        }

        return "events"; // Thymeleaf template name
    }

    @GetMapping("/my_events")
    String showMyEvents(Model model) {
        var eventList = eventService.getAllMyEvents();
        model.addAttribute("eventList", eventList);
        return "my_events";
    }

    @GetMapping("/event/{id}")
    public String showEventById(Model model, @PathVariable Integer id) {
        var event = eventService.findById(id);
        model.addAttribute("event", event);
        return "event";
    }

}