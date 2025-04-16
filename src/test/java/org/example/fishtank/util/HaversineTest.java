package org.example.fishtank.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineTest {

    @Test
    void returnsZeroDistanceForSameCoordinates() {
        double result = Haversine.distance(59.3293, 18.0686, 59.3293, 18.0686);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    void returnsCorrectDistanceBetweenCities() {
        double result = Haversine.distance(59.3293, 18.0686, 57.7089, 11.9746); // Stockholm to Gothenburg
        assertEquals(398, result, 5.0); // within 5km tolerance
    }

    @Test
    void testNaNInput() {
        double distance = Haversine.distance(Double.NaN, 0, 0, 0);
        assertTrue(Double.isNaN(distance));
    }

    @Test
    void testZeroDistance() {
        double distance = Haversine.distance(0, 0, 0, 0);
        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void testValidCoordinatesDoNotThrow() {
        assertDoesNotThrow(() -> Haversine.validateCoordinates(0, 0));
        assertDoesNotThrow(() -> Haversine.validateCoordinates(-90, -180));
        assertDoesNotThrow(() -> Haversine.validateCoordinates(90, 180));
        assertDoesNotThrow(() -> Haversine.validateCoordinates(45.5, 100.0));
    }

    @Test
    void testInvalidLatitudeThrows() {
        assertThrows(IllegalArgumentException.class, () -> Haversine.validateCoordinates(-91, 0));
        assertThrows(IllegalArgumentException.class, () -> Haversine.validateCoordinates(91, 0));
    }

    @Test
    void testInvalidLongitudeThrows() {
        assertThrows(IllegalArgumentException.class, () -> Haversine.validateCoordinates(0, -181));
        assertThrows(IllegalArgumentException.class, () -> Haversine.validateCoordinates(0, 181));
    }

    @Test
    void testBothCoordinatesInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> Haversine.validateCoordinates(-91, 181));
        assertThrows(IllegalArgumentException.class, () -> Haversine.validateCoordinates(91, -181));
    }
}