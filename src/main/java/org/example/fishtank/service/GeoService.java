package org.example.fishtank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class GeoService {

    private static final Logger logger = LoggerFactory.getLogger(GeoService.class);
    // Added HttpClient initialization
    private final HttpClient client = HttpClient.newHttpClient();
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private static final List<String> ALLOWED_HOSTS = List.of("nominatim.openstreetmap.org");

    public Point<G2D> geocodeCity(String cityName) {
        if (cityName == null || cityName.isBlank()) {
            logger.warn("City name is null or blank");
            return null;
        }

        if (!cityName.matches("^[\\p{L}\\p{Z}\\p{P}]+$")) {
            logger.warn("City name contains invalid characters: " + cityName);
            return null;
        }

        // Updated URI construction
        URI uri = URI.create(NOMINATIM_URL + "?q=" + cityName.trim() + "&format=json&limit=1");

        try {
            // Added HttpRequest and HttpResponse handling
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body() == null || response.body().isEmpty()) {
                logger.warn("No response from Nominatim for city: {}", cityName);
                return null;
            }
            // Added JSON parsing using ObjectMapper
            NominatimResponse[] locations = new ObjectMapper().readValue(response.body(), NominatimResponse[].class);
            if (locations.length == 0) {
                logger.warn("No location found for city: {}", cityName);
                return null;
            }

            NominatimResponse location = locations[0];
            return new Point<>(
                    new G2D(
                            Double.parseDouble(location.getLon()),
                            Double.parseDouble(location.getLat())
                    ),
                    CoordinateReferenceSystems.WGS84
            );
        } catch (Exception e) {
            logger.error("Error while geocoding city: {}", cityName, e);
            return null;
        }
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
