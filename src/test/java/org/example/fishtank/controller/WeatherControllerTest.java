package org.example.fishtank.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.example.fishtank.controller.WeatherController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Model model;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    void testNullLocation() {
        String view = weatherController.getWeather(null, model);
        verify(model).addAttribute("error", "No location given, please enter a valid location.");
        assertEquals("weather", view);
    }

    @Test
    void testEmptyLocation() {
        String view = weatherController.getWeather("  ", model);
        verify(model).addAttribute("error", "No location given, please enter a valid location.");
        assertEquals("weather", view);
    }

    @Test
    void testInvalidLocation() {
        String location = "InvalidPlace";
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        String view = weatherController.getWeather(location, model);
        verify(model).addAttribute("error", "Could not find location, try again.");
        assertEquals("weather", view);
    }

    @Test
    void testValidLocation() {
        String location = "Tokyo";
        Map<String, Object> mockResponse = Map.of(
                "current", Map.of(
                        "location", location
                )
        );
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        String view = weatherController.getWeather(location, model);
        verify(model).addAttribute("location", location);
        assertEquals("weather", view);
    }
}
