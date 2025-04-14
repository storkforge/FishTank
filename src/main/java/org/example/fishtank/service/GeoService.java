package org.example.fishtank.service;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeoService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public Point<G2D> geocodeCity(String cityName) {
        if (cityName == null || cityName.isBlank()) {
            return null;
        }

        String url = UriComponentsBuilder.fromUriString(NOMINATIM_URL)
                .queryParam("q", cityName)
                .queryParam("format", "json")
                .queryParam("limit", "1")
                .build()
                .toUriString();

        NominatimResponse[] response = restTemplate.getForObject(url, NominatimResponse[].class);

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