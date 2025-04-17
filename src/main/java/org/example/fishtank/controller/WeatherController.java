package org.example.fishtank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class WeatherController {


    @GetMapping("/weather")

    public String getWeather(@RequestParam(required = false) String location, Model model) {

        String apiUrl = "https://api.weatherapi.com/v1/current.json?key=25c23807bbdb4d399b391221251604&q=" + location;

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class, location);

        if (response != null && response.get("current") != null) {
            Map<String, Object> current = (Map<String, Object>) response.get("current");
            model.addAttribute("location", location);
            model.addAttribute("temperature", current.get("temp_c"));
            model.addAttribute("condition", ((Map<String, Object>) current.get("condition")).get("text"));
        }

        return "weather";
    }
}







