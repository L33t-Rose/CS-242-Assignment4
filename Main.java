import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

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
            double latitute = Double.parseDouble(coordinate[1]);
            double longitude = Double.parseDouble(coordinate[0]);
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

    public static HashMap<String, String> dijkstra(String start, HashMap<String, ArrayList<Edge>> graph) {
        // HashSet
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
        // queue.add(new Edge(0.0,"",))
        while (true) {
            System.out.println(current);
            if (i > count) {
                break;
            }
            ArrayList<Edge> neighbors = graph.get(current);
            for (Edge neighbor : neighbors) {
                // queue.add()
                Double newDistance = distanceMap.get(current) + neighbor.distance;
                if (newDistance < distanceMap.get(neighbor.destination.name)) {
                    distanceMap.put(neighbor.destination.name, newDistance);
                    parentMap.put(neighbor.destination.name,current+","+neighbor.line);
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
        System.out.println("done! Amount of things finished" + i);
        System.out.println("Expected amount: " + count);
        // for (String key : distanceMap.keySet()) {
        //     System.out.print(key + "|");
        //     System.out.print(distanceMap.get(key) + "\n");
        // }
        // System.out.println(distanceMap);
        // for(int i=0;i<count;i++){

        // }
        return parentMap;
    }

    // public static void startDFS(String start, String end, HashMap<String,
    // ArrayList<Edge>> graph) {
    // HashSet<String> visited = new HashSet<>();
    // visited.add(start);
    // String path = start;
    // path += "->" +dfs(start, end, graph, visited,path);
    // System.out.println("Resulting Path: " +path);
    // }

    // public static String dfs(String current, String end,
    // HashMap<String,ArrayList<Edge>> graph, HashSet<String> visited,String path){
    // if(current.equals(end)){
    // return end;
    // }
    // for(Edge neighbor:graph.get(current)){
    // // return dfs(neighbor.destination.)
    // if(!visited.contains(neighbor.destination.name)){
    // path += "->" + neighbor.destination.name + "->" +
    // dfs(neighbor.destination.name,end,graph,visited,path);
    // }
    // }
    // return path;
    // for()
    // HashSet<String> visited = new HashSet<String>();
    // Stack<String> s = new Stack<String>();
    // HashMap<String,LinkedList<String>> a = new HashMap<>();
    // for(){
    // s
    // }
    // a.put()
    // s.add(start);

    // while(!s.isEmpty()){
    // String vertex = s.pop();
    // // Process this somehow
    // System.out.println(vertex);
    // visited.add(vertex);
    // for(Edge neighbor:graph.get(vertex)){
    // System.out.println("current line"+neighbor.line);
    // if(!visited.contains(neighbor.destination.name)){
    // s.add(neighbor.destination.name);
    // }
    // }
    // }
    // }

    public static void dfs(String start, String end, HashMap<String, ArrayList<Edge>> graph) {
        HashSet<String> visited = new HashSet<String>();
        Stack<String> s = new Stack<String>();
        s.add(start);
        ArrayList<String> paths = new ArrayList<String>();
        String path = "";
        while (!s.isEmpty()) {
            String vertex = s.pop();
            // Process this somehow
            // System.out.println(vertex);
            path += "\n" + vertex;
            visited.add(vertex);
            boolean allVisited = true;
            for (Edge neighbor : graph.get(vertex)) {
                // System.out.println("current line"+neighbor.line);
                allVisited = allVisited && visited.contains(neighbor.destination.name);
                if (!visited.contains(neighbor.destination.name)) {
                    s.add(neighbor.destination.name);
                }
            }
            if (allVisited && vertex.equals(end)) {
                paths.add(path);
                // path = start;
            }
            else if(allVisited && !vertex.equals(end)){
                path = start;
            }
        }
        System.out.println(Arrays.toString(paths.toArray()));
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

            // System.out.println(begin.size());
            for (int i = 0; i < begin.size(); i++) {
                Station curr = begin.get(i);
                PriorityQueue<Edge> distanceQueue = new PriorityQueue<Edge>();

                for (int j = 0; j < begin.size(); j++) {
                    Station other = begin.get(j);
                    if (curr.name.equals(other.name)) {
                        continue;
                    }
                    Edge neighbor = new Edge(curr.distanceBetween(other), line, other);
                    distanceQueue.add(neighbor);
                }
                // if (curr.name.equals("Wall St")) {
                // System.out.println(distanceQueue);
                // }
                Edge least = distanceQueue.poll();
                Edge secondLeast = distanceQueue.poll();
                // System.out.println(least + "\n" + secondLeast);
                // Shifting our second least until it's below our current
                // while (!distanceQueue.isEmpty() && ((curr.latitude <
                // least.destination.latitude
                // && curr.latitude < secondLeast.destination.latitude)
                // || (curr.latitude > least.destination.latitude
                // && curr.latitude > secondLeast.destination.latitude))) {
                // secondLeast = distanceQueue.poll();
                // }

                while (!distanceQueue.isEmpty()) {
                    boolean incorrectOption1 = curr.latitude < secondLeast.destination.latitude
                            && curr.latitude < least.destination.latitude;
                    boolean incorrectOption2 = curr.latitude > secondLeast.destination.latitude
                            && curr.latitude > least.destination.latitude;
                    // System.out.println(incorrectOption1 || incorrectOption2);
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

        listToJSON(adjacencyList);
        // System.out.println("J Train Lines");

        HashMap<String,String> output = dijkstra(start, adjacencyList);
        // System.out.println(output);
        // for(String key:output.keySet()){
        //     System.out.println(key+":"+output.get(key));
        // }
        String next = output.get(end);
        System.out.println(Arrays.toString(next.split(",")));
        while(next != null){
            System.out.println(output.get(next.split(",")[0]));
            next = output.get(next.split(",")[0]);
        }
        // startDFS(start,end, adjacencyList);
        // dfs(start, end, adjacencyList);
        sc.close();
    }
}