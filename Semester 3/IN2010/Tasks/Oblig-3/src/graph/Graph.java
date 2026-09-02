package graph;

import node.*;

import java.util.*;

public class Graph {

    // Variables
    private final HashMap<Node, HashSet<Node>> edges = new HashMap<>();
    private final HashSet<HashSet<Node>> components = new HashSet<>();
    private int edgeCount;

    // Note that all the actors are in the edges as keys,
    // so there is no need for a node set (it is redundant).
    // Same goes for movies since all of them are in at least
    // one actor's movie set that they played in. And yes,
    // even actors who didn't play in any movie are there.

    // But, these are still useful when counting. So what we do
    // is we use them as keys for two of our edge set variations.

    // I am also keeping this to make stuff faster (check below)
    private final HashSet<Actor> actors;


    // Constructor
    public Graph(HashMap<String, Movie> movies, HashMap<String, Actor> actors) {

        // Keeping some data for optimization
        this.actors = new HashSet<>(actors.values());

        // Mapping and counting
        mapEdges(movies, actors);  // Mapping edges
        getComponents();  // Getting components
    }

    // Static Methods
    public static String pathToString(HashMap<Node, Node> path, Actor startActor) {

        // Adding starting actor
        String result = startActor.getName();

        // Printing out each step (each node taken in order)
        Node step = path.get(startActor);
        while (step != null) {

            // Adding step to result string
            if (step.getClass() == Movie.class) {  // Adding movie
                result = result.concat("\n===[ " + step.getName() + " " + ((Movie) step).getRating() + " ] ===> ");
            } else if (step.getClass() == Actor.class) {  // Adding actor
                result = result.concat(step.getName());
            }
            // Next step
            step = path.get(step);
        }
        // Returning result string
        return result;
    }
    public static String chillestPathToString(HashMap<Node, Node> path, Actor startActor) {
        // Similar to pathToString() method

        // Adding starting actor
        String result = startActor.getName();

        // Setup
        Node step = path.get(startActor);
        float totalWeight = 0f;

        // Printing out each step (each node taken in order)
        while (step != null) {

            // Adding step to result string
            if (step.getClass() == Movie.class) {  // Adding movie
                Movie movie = (Movie) step;  // No idea how time-consuming casting is
                result = result.concat("\n===[ " + movie.getName() + " " + movie.getRating() + " ] ===> ");
                totalWeight += movie.getRating();
            } else if (step.getClass() == Actor.class) {  // Adding actor
                result = result.concat(step.getName());
            }
            // Next step
            step = path.get(step);
        }
        // Final touch
        totalWeight = totalWeight * 5 / 5;  // Lol what? I guess it works...
        result = result.concat("\nTotal weight: " + totalWeight);
        // P.S. try to comment out line 85, and you will understand.
        // It turns out computers can be bad at math too.

        // Returning result string
        return result;
    }

    // Helper methods
    private void mapEdges(HashMap<String, Movie> movies, HashMap<String, Actor> actors) {
        edgeCount = 0;

        // Mapping in form of neighbour lists (both movie and actor edges simultaneously)
        for (Actor actor: actors.values()) {
            HashSet<Node> playedIn = new HashSet<>();  // Set of movies actor played in

            // Locating movies based on which ones actor played in
            for (String TTID : actor.getTTIDs()) {
                Movie movie = movies.get(TTID);
                if (movie != null) {  // Ignoring TTIDs that are not in movies

                    // adding movie to the actor's edge set
                    playedIn.add(movie);

                    // Adding actor to the movie's edge set
                    if (!edges.containsKey(movie)) {
                        edges.put(movie, new HashSet<>());
                    } edges.get(movie).add(actor);

                    // Incrementing edge count by one (new edge was added)
                    edgeCount++;
                }
            }  // Adding actor and its edge set
            edges.put(actor, playedIn);
        }
    }
    private void getComponents() {
        // Using DFS (Depth First Search)

        // Keeping track of visited nodes
        HashSet<Node> visited = new HashSet<>();

        // Initializing DFS
        for (Actor actor : actors) {

            // Node is already in some component, skip
            if (visited.contains(actor)) {
                continue;
            } visited.add(actor);  // Otherwise add it (we are visiting it now)

            // Algorithm prep
            HashSet<Node> component = new HashSet<>();
            Stack<Node> stack = new Stack<>();
            component.add(actor);
            stack.add(actor);

            // Algorithm launch
            while (!stack.isEmpty()) {
                Node current = stack.pop();
                for (Node next : edges.get(current)) {

                    // Node already visited, skip
                    if (visited.contains(next)) {
                        continue;
                    }  // P.S. I hate how many times we must check that

                    // Adding new node
                    visited.add(next);
                    stack.push(next);
                    if (next.getClass() == Actor.class) {  // Note, we only count actors
                        component.add(next);
                    }
                }
            }

            // Adding component to set
            components.add(component);
        }
    }
    private float getWeight(Node nodeA, Node nodeB) {
        float rating;
        if (nodeA.getClass() == Movie.class) {
            rating = ((Movie) nodeA).getRating();
        } else if (nodeB.getClass() == Movie.class) {
            rating = ((Movie) nodeB).getRating();
        } else {
            rating = 0f;
        } return Movie.maxRating - rating;
        // This one's pretty simple, hope I don't have to explain it
    }

    // Methods
    public int getNodeCount() {
        return edges.keySet().size();  // It is in O(1) anyways
    }
    public int getEdgeCount() {
        return edgeCount;
    }
    public HashMap<Integer, Integer> countComponents() {

        // Counting how many are of same size
        HashMap<Integer, Integer> componentsCount = new HashMap<>();
        for (HashSet<Node> component : components) {
            int componentSize = component.size();

            // New size, add to counts
            if (!componentsCount.containsKey(componentSize)) {
                componentsCount.put(componentSize, 0);
            } // Counting up
            componentsCount.put(componentSize, componentsCount.get(componentSize) + 1);
        }
        // Returning result
        return componentsCount;
    }
    public HashMap<Node, Node> getPath(Actor startActor, Actor endActor) {
        // Implementing BFS (Breadth First Search)

        // Setup #1
        HashSet<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        HashMap<Node, Node> previous = new HashMap<>();

        // Setup #2
        queue.offer(startActor);
        visited.add(startActor);

        // Launching algorithm
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Actor found, terminating
            if (current.equals(endActor)) {
                break;
            }

            // Depth step up
            for (Node next : edges.get(current)) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }  // Reconstructing path
        HashMap<Node, Node> path = new HashMap<>();
        Node step = endActor;
        while (previous.containsKey(step)) {
            path.put(previous.get(step), step);
            step = previous.get(step);
        }
        // Returning result
        return path;
    }
    public HashMap<Node, Node> getChillestPath(Actor startActor, Actor endActor) {
        // Implementing Djikstra's algorithm as requested

        // Setup #1
        HashMap<Node, Float> distances = new HashMap<>();
        HashSet<Node> visited = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(distances::get));
        HashMap<Node, Node> previous = new HashMap<>();

        // Setup #2
        for (Node node : edges.keySet()) {
            distances.put(node, Float.MAX_VALUE);
        } distances.put(startActor, 0f);
        queue.offer(startActor);

        // Launch
        while (!queue.isEmpty()) {
            Node current = queue.poll();  // Getting node

            // If best path is already found, terminate
            if (current == endActor) {
                break;
            }

            // Skipping nodes we already visited
            if (visited.contains(current)) {
                continue;
            } visited.add(current);  // Otherwise adding it

            // Updating distances
            for (Node next : edges.get(current)) {
                float distance = distances.get(current) + getWeight(current, next);
                if (distance < distances.get(next)) {
                    distances.put(next, distance);
                    queue.offer(next);
                    previous.put(next, current);
                }
            }
        }  // Getting path
        HashMap<Node, Node> path = new HashMap<>();
        Node step = endActor;
        while (previous.containsKey(step)) {  // Similar to pathToString()
            path.put(previous.get(step), step);
            step = previous.get(step);
        }
        // Returning path
        return path;
    }
}
