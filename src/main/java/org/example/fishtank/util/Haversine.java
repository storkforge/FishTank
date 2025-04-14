package org.example.fishtank.util;

/**
 * + * Utility class for calculating great-circle distances between two points on Earth
 * + * using the Haversine formula.
 * +
 */
public class Haversine {

    private static final int EARTH_RADIUS_KM = 6371;

    /**
     * +    * Calculates the distance between two geographical points using the Haversine formula.
     * +    *
     * +    * @param lat1 latitude of the first point in degrees
     * +    * @param lng1 longitude of the first point in degrees
     * +    * @param lat2 latitude of the second point in degrees
     * +    * @param lng2 longitude of the second point in degrees
     * +    * @return distance between the points in kilometers
     * +    * @throws IllegalArgumentException if coordinates are not within valid ranges
     * +
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {

        validateCoordinates(lat1, lng1);
        validateCoordinates(lat2, lng2);

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    /**
     * Validates that coordinates are within valid ranges.
     *
     * @param lat latitude (-90 to 90)
     * @param lng longitude (-180 to 180)
     * @throws IllegalArgumentException if coordinates are out of range
     */
    private static void validateCoordinates(double lat, double lng) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (lng < -180 || lng > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }
}