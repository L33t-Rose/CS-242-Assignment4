public class Station {
    String name;
    double latitude;
    double longitude;

    Station(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name + "|" + latitude + "|" + longitude;
    }
}
