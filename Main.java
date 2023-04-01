import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class Main {
    public static class Edge implements Comparable<Edge> {
        double distance;
        String line;
        Station destination;

        public Edge(int distance, String line, Station destination) {
            this.distance = distance;
            this.line = line;
            this.destination = destination;
        }

        @Override
        public int compareTo(Edge o) {
            // TODO Auto-generated method stub
            if (this.distance > o.distance) {
                return 1;
            } else if (this.distance < o.distance) {
                return -1;
            }
            return 0;
        }
    }
    // Key- line; V-
    public static HashMap<String, ArrayList<Station>> readFromCSV() {
        System.out.println("Running");
        Scanner sc = null;
        try {
            sc = new Scanner(new File("mta_stations.csv"));
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        System.out.println("here");
        HashMap<String, ArrayList<Station>> NYCSubwayMap = new HashMap<String, ArrayList<Station>>();
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] data = sc.nextLine().split(",");
            String name = data[2];
            String[] coordinate = data[3].replaceAll("POINT ", "").replace("(", "").replace(")","").split(" ");
            double latitute = Double.parseDouble(coordinate[0]);
            double longitude = Double.parseDouble(coordinate[1]);
            Station stat = new Station(name, latitute, longitude);
            String[] lines = data[4].split("-");
            System.out.println(Arrays.toString(lines));
            for(String line:lines){
                if(!NYCSubwayMap.containsKey(line)){
                    NYCSubwayMap.put(line,new ArrayList<Station>());
                }
                NYCSubwayMap.get(line).add(stat);
            }
        }
        sc.close();
        return NYCSubwayMap;
    }

    public static void main(String[] args) {
        HashMap<String, ArrayList<Station>> a = readFromCSV();
        for(Station line:a.get("7")){
            System.out.println(line);
            // System.out.println(line+":"+Arrays.toString(a.get(line).toArray()));
        }
    }
}