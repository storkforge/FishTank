package org.example.fishtank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class WeatherController {

    private final RestTemplate restTemplate;


    public WeatherController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping("/weather")
    public String getWeather(@RequestParam(required = false) String location, Model model) {
        if (location == null || location.trim().isEmpty()) {
            model.addAttribute("error", "No location given, please enter a valid location.");
            return "weather";
        }

        String apiUrl = "https://api.weatherapi.com/v1/current.json?key=25c23807bbdb4d399b391221251604&q=" + location;

        try {
            Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

            if (response == null || response.get("current") == null) {
                model.addAttribute("error", "Could not find location, try again.");
            } else {
                Map<String, Object> current = (Map<String, Object>) response.get("current");
                model.addAttribute("location", location);
                model.addAttribute("temperature", current.get("temp_c"));
                model.addAttribute("condition", ((Map<String, Object>) current.get("condition")).get("text"));
            }

        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Error retrieving weatherdata. Check location and try again.");
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error retrieving weatherdata.");
        }

        return "weather";
    }
}







