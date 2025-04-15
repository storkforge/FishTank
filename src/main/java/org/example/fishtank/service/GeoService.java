package org.example.fishtank.service;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class GeoService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private static final List<String> ALLOWED_HOSTS = List.of("nominatim.openstreetmap.org");


    public Point<G2D> geocodeCity(String cityName) {
        if (cityName == null || cityName.isBlank()) {
            return null;
        }

        if (!cityName.matches("^[\\p{L}\\p{Z}\\p{P}]+$")) {
            return null;
        }

        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", cityName.trim())
                .queryParam("format", "json")
                .queryParam("limit", "1")
                .build()
                .toUri();

        NominatimResponse[] response = restTemplate.getForObject(uri, NominatimResponse[].class);


        if (response == null || response.length == 0) {
            return null;
        }

        NominatimResponse location = response[0];
        return new Point<>(
                new G2D(
                        Double.parseDouble(location.getLon()),
                        Double.parseDouble(location.getLat())
                ),
                CoordinateReferenceSystems.WGS84
        );
    }

    private static class NominatimResponse {
        private String lat;
        private String lon;

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }
    }
}