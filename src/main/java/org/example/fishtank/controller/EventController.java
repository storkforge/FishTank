package org.example.fishtank.controller;

import org.example.fishtank.model.dto.eventDto.CreateEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEvent;
import org.example.fishtank.model.dto.eventDto.ResponseEventList;
import org.example.fishtank.model.dto.eventDto.UpdateEvent;
import org.example.fishtank.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
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
        model.addAttribute("event", new CreateEvent("temp", "temp", LocalDateTime.now()));
        return "add_event";
    }

    @PostMapping("/add_event")
    public String addEvent(
            @RequestParam("text") String text,
            @RequestParam("cityName") String cityName,
            @RequestParam("eventDate") LocalDateTime eventDate
    ) {
        CreateEvent createEvent = new CreateEvent(text, cityName, eventDate);
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
            @RequestParam("text") String text,
            @RequestParam(value = "cityName", required = false) String cityName,
            @RequestParam(value = "eventDate", required = false) LocalDateTime eventDate) {
        eventService.update(id, new UpdateEvent(text, cityName, eventDate));
        return "redirect:/events";
    }

    @PostMapping("/delete_event/{id}")
    public String deleteEvent(@PathVariable Integer id) {
        eventService.delete(id);
        return "redirect:/events";
    }

    @GetMapping("/events")
    public String showEvents(Model model) {
        var eventList = eventService.getAllEvents();
        model.addAttribute("eventList", eventList);
        return "events";
    }

}