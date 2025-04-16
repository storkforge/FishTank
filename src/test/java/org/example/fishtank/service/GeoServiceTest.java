package org.example.fishtank.service;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeoServiceTest {

    @InjectMocks
    GeoService geoService;

    @Mock
    HttpClient mockHttpClient;

    @Mock
    HttpResponse<String> mockResponse;


    @Test
    void geocodeCityValidCityName() throws Exception {
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn("[{\"lat\":\"40.7128\",\"lon\":\"-74.0060\"}]");
        when(mockHttpClient.send(any(), any())).thenReturn(mockResponse);

        Point<G2D> point = geoService.geocodeCity("New York");

        assertNotNull(point);
        assertEquals(-74.0060, point.getPosition().getLon(), 0.0001);
        assertEquals(40.7128, point.getPosition().getLat(), 0.0001);
    }

    @Test
    void geocodeCityInvalidCityName() throws Exception {
        when(mockResponse.body()).thenReturn("");
        when(mockHttpClient.<String>send(any(), any())).thenReturn(mockResponse);

        Point<G2D> point = geoService.geocodeCity("Invalid city!");

        assertNull(point);
    }

    @Test
    void geocodeCityIsNull() throws Exception {
        Point<G2D> point = geoService.geocodeCity(null);
        assertNull(point);
    }

    @Test
    void geocodeCityIsBlank() throws Exception {
        Point<G2D> point = geoService.geocodeCity("   ");
        assertNull(point);
    }

    @Test
    void geocodeCityNoResponse() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Point<G2D> point = geoService.geocodeCity("New York");

        assertNull(point);
    }

    @Test
    void geocodeCityExceptionHandling() throws Exception {
        when(mockHttpClient.send(any(), any())).thenThrow(new RuntimeException("HTTP error"));

        Point<G2D> point = geoService.geocodeCity("New York");

        assertNull(point);
    }

}