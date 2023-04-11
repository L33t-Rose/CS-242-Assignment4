import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
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

            // return (int) (this.distance - o.distance);
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return line + "|" + destination.toString() + "|" + distance;
        }

        @Override
        public boolean equals(Object obj) {
            return (this.distance == ((Edge) obj).distance) && this.destination.equals(((Edge) obj).destination);
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

    public static void listToJSON(HashMap<String, ArrayList<Edge>> list) {
        File newFile = new File("subway.json");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            FileWriter json = new FileWriter(newFile);
            StringBuilder output = new StringBuilder("{\n");
            for (String key : list.keySet()) {
                ArrayList<Edge> data = list.get(key);
                output.append("\"" + key + "\":[");
                for (Edge e : data) {
                    output.append("\"" + e.toString() + "\",");
                }
                output.append("],\n");
            }
            output.append("}");
            json.write(output.toString());
            json.close();
        } catch (IOException e) {
            System.out.println(e);
        }

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

        HashMap<String, ArrayList<Edge>> adjacencyList = new HashMap<>();

        for (String line : subwayLineMap.keySet()) {
            ArrayList<Station> begin = subwayLineMap.get(line);

            System.out.println(begin.size());
            for (int i = 0; i < begin.size(); i++) {
                Station curr = begin.get(i);
                PriorityQueue<Edge> distanceQueue = new PriorityQueue<Edge>();
                // double minDistance = Integer.MAX_VALUE;
                // Station possibleEnd = null;

                for (int j = 0; j < begin.size(); j++) {
                    Station other = begin.get(j);
                    if (curr.name.equals(other.name)) {
                        continue;
                    }
                    Edge neighbor = new Edge(curr.distanceBetween(other), line, other);
                    distanceQueue.add(neighbor);
                    // if (curr.distanceBetween(other) < minDistance) {
                    // minDistance = curr.distanceBetween(other);
                    // possibleEnd = other;
                    // }
                }
                if(curr.name.equals("Wall St")
                ){
                    System.out.println(distanceQueue);
                }
                Edge least = distanceQueue.poll();
                Edge secondLeast = distanceQueue.poll();
                System.out.println(least +"\n"+secondLeast);
                while(!distanceQueue.isEmpty() && ((curr.longitude < least.destination.longitude && curr.longitude < secondLeast.destination.longitude) || (curr.longitude > least.destination.longitude && curr.longitude > secondLeast.destination.longitude))){
                    secondLeast = distanceQueue.poll();
                }
                if (!adjacencyList.containsKey(curr.name)) {
                    adjacencyList.put(curr.name, new ArrayList<Edge>());
                }

                adjacencyList.get(curr.name).add(least);
                adjacencyList.get(curr.name).add(secondLeast);

                // double secondLeast = minDistance;
                // for()
                // System.out.println("Current Station: " + curr.name + "\nPossible End: " +
                // possibleEnd.name);
                // Edge a = new Edge(minDistance, "2", possibleEnd);
                // Edge b = new Edge(minDistance, "2", curr);
                // if (!adjacencyList.containsKey(curr.name)) {
                // adjacencyList.put(curr.name, new ArrayList<Edge>());
                // }
                // if (!adjacencyList.containsKey(possibleEnd.name)) {
                // adjacencyList.put(possibleEnd.name, new ArrayList<Edge>());
                // }
                // if (!adjacencyList.get(curr.name).contains(a)) {
                // adjacencyList.get(curr.name).add(a);
                // }
                // if (!adjacencyList.get(possibleEnd.name).contains(b)) {
                // adjacencyList.get(possibleEnd.name).add(b);
                // }
            }
        }

        listToJSON(adjacencyList);
        System.out.println("J Train Lines");

        for (Station a : subwayLineMap.get("J")) {
            System.out.println(a);
        }

        System.out.println("Fulton St");
        sc.close();
    }
}