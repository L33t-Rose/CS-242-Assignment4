import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

// import javax.print.DocFlavor.INPUT_STREAM;

class Main {
    public static class Edge implements Comparable<Edge> {
        double distance;
        String line;
        Station destination;

        public Edge(double distance, String line, Station destination) {
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

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return line + "|" + destination.toString();
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
            String[] coordinate = data[3].replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ");
            double latitute = Double.parseDouble(coordinate[0]);
            double longitude = Double.parseDouble(coordinate[1]);
            String stationLines = data[4];
            Station stat = new Station(name, latitute, longitude, stationLines);
            String[] lines = data[4].split("-");
            System.out.println(Arrays.toString(lines));
            for (String line : lines) {
                if (!NYCSubwayMap.containsKey(line)) {
                    NYCSubwayMap.put(line, new ArrayList<Station>());
                }
                NYCSubwayMap.get(line).add(stat);
            }
        }
        sc.close();
        return NYCSubwayMap;
    }

    public static void sortStation(ArrayList<Station> a) {
        for (int i = 0; i < 1; i++) {
            int MIN_INDEX = i;
            double minDistance = 0;
            for (int j = i + 1; j < a.size() - 1; j++) {
                // System.out.println(a.get(MIN_INDEX).compareTo(a.get(j)));
                double distance = a.get(i).distanceBetween(a.get(j));
                // if(distance - )
            }
            // Station temp = a.get(i);
            // a.set(i, a.get(MIN_INDEX));
            // a.set(MIN_INDEX, temp);
        }
        // System.out.println(a);
        // return a;
    }

    public static void main(String[] args) {
        HashMap<String, ArrayList<Station>> subwayLineMap = readFromCSV();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Station You're Starting At");
        String start = sc.nextLine();
        System.out.println("Enter Destination");
        String end = sc.nextLine();
        if (start.equals(end)) {
            System.out.println("this is the same station");
        }

        ArrayList<String> possibleLines = new ArrayList<String>();
        for (String key : subwayLineMap.keySet()) {
            ArrayList<Station> line = subwayLineMap.get(key);
            // if(line.contains(end))
            int count = 0;
            for (Station s : line) {
                if (s.name.equals(start) || s.name.equals(end)) {
                    count++;
                }
            }
            if (count == 2) {
                possibleLines.add(key);
            }
        }
        System.out.println(possibleLines);
        HashMap<Station, ArrayList<Edge>> adjacencyList = new HashMap<>();

        for (String line : subwayLineMap.keySet()) {
            ArrayList<Station> begin = subwayLineMap.get(line);

            System.out.println(begin.size());
            // int middle = begin.size() / 2;
            // Station s = begin.get(0);
            // Station s = new Station("fuck this",0,0,"");
            for (int i = 0; i < begin.size(); i++) {
                Station curr = begin.get(i);
                double minDistance = Integer.MAX_VALUE;
                // System.out.println(d);
                Station possibleEnd = null;

                for (int j = 0; j < begin.size(); j++) {
                    Station other = begin.get(j);
                    if (curr.name.equals(other.name)) {
                        continue;
                    }
                    if (curr.distanceBetween(other) < minDistance) {
                        minDistance = curr.distanceBetween(other);
                        possibleEnd = other;
                    }
                }
                // System.out.println(d+", distance between: "+ );
                System.out.println("Current Station: " + curr.name + "\nPossible End: " + possibleEnd.name);
                Edge a = new Edge(minDistance,line,possibleEnd);
                if(!adjacencyList.containsKey(curr)){
                    adjacencyList.put(curr,new ArrayList<Edge>());
                }
                adjacencyList.get(curr).add(a);
            }
        }
        System.out.println("J Train Lines");
        
        for(Station a:subwayLineMap.get("J")){
            System.out.println(a);
        }

        for(Station test: adjacencyList.keySet()){
            System.out.println(test);
            for(Edge e:adjacencyList.get(test)){
                System.out.println(e);
            }
            System.out.println("---------------");
        }
        // Station fulton = subwayLineMap.get("5").get(41);
        // for(Edge e:adjacencyList.get(fulton)){
        //     System.out.println(e);
        // }

        // for()
        // sortStation(a.get("7 Express"));
        // for(String key: a.keySet() ){
        // for(String line:a.get(key)){

        // }
        // System.out.println(line);
        // // System.out.println(line+":"+Arrays.toString(a.get(line).toArray()));
        // }
        // for(String key: a.keySet()){
        // System.out.println(key);
        // for(Station line:a.get(key)){
        // System.out.println(line);
        // }
        // System.out.println("---------------");
        // }
    }
}