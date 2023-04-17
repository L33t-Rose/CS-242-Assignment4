import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

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
            return line + "|" + destination.toString() + "|" + distance;
        }

        @Override
        public boolean equals(Object obj) {
            return (this.distance == ((Edge) obj).distance) && this.destination.equals(((Edge) obj).destination);
        }
    }

    public static HashMap<String, ArrayList<Station>> readFromCSV() {
        Scanner sc = null;
        try {
            sc = new Scanner(new File("mta_stations.csv"));
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        HashMap<String, ArrayList<Station>> NYCSubwayMap = new HashMap<String, ArrayList<Station>>();
        sc.nextLine();
        while (sc.hasNextLine()) {
            String[] data = sc.nextLine().split(",");
            String name = data[2];
            String[] coordinate = data[3].replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ");
            double latitute = Double.parseDouble(coordinate[1]);
            double longitude = Double.parseDouble(coordinate[0]);
            String stationLines = data[4];
            Station stat = new Station(name, latitute, longitude, stationLines);
            String[] lines = data[4].split("-");
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

    public static HashMap<String, String> dijkstra(String start, String end, HashMap<String, ArrayList<Edge>> graph) {
        HashMap<String, Double> distanceMap = new HashMap<>();
        HashMap<String, String> parentMap = new HashMap<>();
        HashMap<String, Boolean> visitedMap = new HashMap<>();

        for (String key : graph.keySet()) {
            distanceMap.put(key, Double.MAX_VALUE);
            visitedMap.put(key, false);
        }
        distanceMap.put(start, 0.0);
        String current = start;
        int count = graph.keySet().size();
        int i = 0;
        PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
        while (true) {
            if (i > count) {
                break;
            }
            ArrayList<Edge> neighbors = graph.get(current);
            for (Edge neighbor : neighbors) {
                Double newDistance = distanceMap.get(current) + neighbor.distance;
                int TimeToTransferInMinutes = 1;
                if (parentMap.get(current) != null) {
                    if (!neighbor.destination.line.equals(parentMap.get(current).split(",")[1])) {
                        newDistance += TimeToTransferInMinutes;
                    }
                }
                if (newDistance < distanceMap.get(neighbor.destination.name)) {
                    distanceMap.put(neighbor.destination.name, newDistance);
                    parentMap.put(neighbor.destination.name, current + "," + neighbor.line);
                }
                if (!visitedMap.get(neighbor.destination.name)) {
                    neighbor.distance = newDistance;
                    queue.add(neighbor);
                }
            }
            visitedMap.put(current, true);
            if (queue.isEmpty()) {
                break;
            }
            current = queue.poll().destination.name;
            while (!queue.isEmpty() && visitedMap.get(current)) {
                current = queue.poll().destination.name;
            }
            i++;
        }
        // parentMap.put(start,start);
        double result = distanceMap.get(end);
        int timeInMinutes = (int) result / 1;
        int timeInSeconds = (int) (result % 1 * 60);

        System.out.println(
                "Time to get from " + start + " to " + end + ": " + timeInMinutes + "min " + timeInSeconds + "sec");
        return parentMap;
    }

    public static void printPath(String end,HashMap<String,String> parentMap){
        if(end == parentMap.get(end)){
            return;
        }
        // System.out.println(end);
        // String[] next = parentMap.get(end).split(",");
        printPath(parentMap.get(end).split(",")[0],parentMap);
        // System.out.println(next);
        System.out.println(end);
        // return end;
    }

    public static String getInput(Scanner sc, HashMap<String, ArrayList<Edge>> subwayLineMap, String message) {
        boolean validInput = false;
        String input = "";
        do {
            try {
                System.out.println(message);
                input = sc.nextLine();
                if (!subwayLineMap.containsKey(input)) {
                    throw new Exception();
                }
                validInput = true;
            } catch (Exception e) {
                System.out.println("That's not a real station! Try Again!");
                validInput = false;
                ArrayList<String> possibleStations = new ArrayList<String>();
                for (String station : subwayLineMap.keySet()) {
                    if (station.toLowerCase().indexOf(input.toLowerCase()) != -1) {
                        possibleStations.add(station);
                    }
                }
                if (possibleStations.size() != 0) {
                    System.out.println("Here are all the possible stations for you to select from!");
                    // System.out.println(Arrays.toString(possibleStations.toArray()));
                    for (int i = 0; i < possibleStations.size(); i++) {
                        System.out.println(i + 1 + ": " + possibleStations.get(i));
                    }
                    int index = 0;
                    do {
                        System.out.println("Please choose a number corresponding to the station you want to start at");
                        try {
                            index = sc.nextInt() - 1;
                        } catch (Exception a) {
                            System.out.println("Not a valid number!");
                            index = -1;
                            sc.nextLine();
                        }
                    } while (index < 0 || index > possibleStations.size());
                    input = possibleStations.get(index);
                    validInput = true;
                }
            }
        } while (!validInput);
        sc.nextLine();
        return input;
    }

    public static void main(String[] args) {
        HashMap<String, ArrayList<Station>> subwayLineMap = readFromCSV();
        HashMap<String, ArrayList<Edge>> adjacencyList = new HashMap<>();

        for (String line : subwayLineMap.keySet()) {
            ArrayList<Station> begin = subwayLineMap.get(line);

            for (int i = 0; i < begin.size(); i++) {
                Station curr = begin.get(i);
                PriorityQueue<Edge> distanceQueue = new PriorityQueue<Edge>();

                for (int j = 0; j < begin.size(); j++) {
                    Station other = begin.get(j);
                    if (curr.name.equals(other.name)) {
                        continue;
                    }
                    // Idea is that if we have speed and distance than we get time by dividing
                    // distance / speed. -> speed = distance / time. 
                    // Solving for time our equation is time = distance / speed
                    // A search online shows that the verage train speed is 17.4 miles per four
                    // We'll change up that figure to be meters per minute since we assume it takes ~1 minute
                    // to transfer
                    double AverageTrainSpeedInMetersPerMinute = 466.7098;
                    Edge neighbor = new Edge(curr.distanceBetween(other) / AverageTrainSpeedInMetersPerMinute, line,
                            other);
                    distanceQueue.add(neighbor);
                }

                Edge least = distanceQueue.poll();
                Edge secondLeast = distanceQueue.poll();

                while (!distanceQueue.isEmpty()) {
                    boolean incorrectOption1 = curr.latitude < secondLeast.destination.latitude
                            && curr.latitude < least.destination.latitude;
                    boolean incorrectOption2 = curr.latitude > secondLeast.destination.latitude
                            && curr.latitude > least.destination.latitude;
                    if (!(incorrectOption1 || incorrectOption2)) {
                        break;
                    }
                    secondLeast = distanceQueue.poll();
                }
                if (!adjacencyList.containsKey(curr.name)) {
                    adjacencyList.put(curr.name, new ArrayList<Edge>());
                }
                adjacencyList.get(curr.name).add(least);
                if (!distanceQueue.isEmpty()) {
                    adjacencyList.get(curr.name).add(secondLeast);
                }
            }
        }

        Scanner sc = new Scanner(System.in);
        String start = getInput(sc, adjacencyList, "Enter What Station You're Starting At:");
        String end = getInput(sc, adjacencyList, "Enter Your Destination:");
        System.out.println("Selected Your Beginning Station To Be: " + start);
        System.out.println("Selected Your Destination Station To Be: " + end);

        if (start.equals(end)) {
            System.out.println("this is the same station");
        }

        ArrayList<String> possibleLines = new ArrayList<String>();
        for (String key : subwayLineMap.keySet()) {
            ArrayList<Station> line = subwayLineMap.get(key);
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
        
        listToJSON(adjacencyList);
        HashMap<String, String> output = dijkstra(start, end, adjacencyList);
        // printPath(end, output);
        ArrayList<String> path = new ArrayList<>();

        String next = output.get(end);
        while (next != null) {
            path.add(next);
            next = output.get(next.split(",")[0]);
        }
        Collections.reverse(path);
        for(String i:path){
            String[] info = i.split(",");
            if(info.length == 2){
                System.out.print("From "+info[0]+" ");
                System.out.print("Take the "+info[1]+" train\n|\nv\n");
            }else{
                System.out.println("You've arrived at your destination: "+info[0]);
            }

            // System.out.println(i);
        }
        System.out.println(end);
        sc.close();
    }
}