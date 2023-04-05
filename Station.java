public class Station implements Comparable<Station> {
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
        // TODO Auto-generated method stub
        return name + "| (" + latitude + ", " + longitude + ")";
    }

    public double distanceBetween(Station other){
        double a = Math.pow(other.latitude - this.latitude,2);
        double b = Math.pow(other.longitude-this.longitude,2);
        return Math.sqrt(a+b);
    }

    @Override
    public int compareTo(Station o) {
        // TODO Auto-generated method stub
        // return (int)(this.longitude - o.longitude);
        return 1;
    }
}
