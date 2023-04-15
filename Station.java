public class Station{
    String name;
    double latitude;
    double longitude;
    String line;

    Station(String name, double latitude, double longitude, String line) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.line = line;
    }

    @Override
    public String toString() {
        return name + "| (" + latitude + ", " + longitude + ")";
    }

    private static final double EARTH_RADIUS_KM = 6371e3;

    private static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = toRadians(Math.toRadians(lat2) - Math.toRadians(lat1));
        double dLon = toRadians(Math.toRadians(lon2) - Math.toRadians(lon1));
        double a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)) +
                   (Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2))) *
                   (Math.sin(dLon / 2) * Math.sin(dLon / 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        return distance; // in meters;
    }

    public double distanceBetween(Station other){
        return calculateDistance(latitude, longitude, other.latitude, other.longitude);
    }
}
