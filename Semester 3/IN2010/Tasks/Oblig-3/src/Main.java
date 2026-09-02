import graph.*;
import node.*;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        //   --- Setup ---
        // Reading file data
        HashMap<String, Movie> movies = Movie.getMovies("./data/movies.tsv");
        HashMap<String, Actor> actors = Actor.getActors("./data/actors.tsv");
        // P.S. I think it looks better this way However,
        // I could just add it to graph's constructor.

        // Creating graph (edges) based on nodes that we got
        // We also discard actor TTIDs that don't exist in movies
        Graph graph = new Graph(movies, actors);

        //   --- Counting ---
        // Counting nodes and edges
        System.out.println("Nodes & Edges:\nNodes: " + graph.getNodeCount() + "\nEdges: " + graph.getEdgeCount());
        // Note that we get a different result than the one in Oblig 3 and that is because
        // unlike someone I did ignore the cases where some TTIDs that we got from actors
        // did not exist in the movies.tsv file as requested :p

        // Counting components
        System.out.println("\nComponents & Their Sizes:");
        HashMap<Integer, Integer> componentsCount = graph.countComponents();
        for (int componentSize : componentsCount.keySet()) {
            System.out.println("There are " + componentsCount.get(componentSize) + " components of size " + componentSize);
        } // I am not sure if it is too slow or not, but it finished in like 5 seconds for me
        // It is still far from instant which is what I was trying to aim for (somewhat)

        //   --- Paths ---
        // Preparing valid queries
        HashMap<Actor, Actor> queries = new HashMap<>();
        queries.put(actors.get("nm2255973"), actors.get("nm0000460"));
        queries.put(actors.get("nm0424060"), actors.get("nm8076281"));
        queries.put(actors.get("nm4689420"), actors.get("nm0000365"));
        queries.put(actors.get("nm0000288"), actors.get("nm2143282"));
        queries.put(actors.get("nm0637259"), actors.get("nm0931324"));

        // Finding all requested paths (assuming they exist)
        HashMap<Actor, HashMap<Node, Node>> paths = new HashMap<>();
        for (Actor startActor : queries.keySet()) {
            Actor endActor = queries.get(startActor);
            HashMap<Node, Node> path = graph.getPath(startActor, endActor);
            paths.put(startActor, path);
        }

        // Printing out the result (paths)
        System.out.println("\nRequested Paths:");
        for (Actor startActor : paths.keySet()) {

            // Printing path
            HashMap<Node, Node> path = paths.get(startActor);
            String pathString = Graph.pathToString(path, startActor);
            System.out.println(pathString + "\n");
        }
        // As you can see the movies and actors vary, but that is only
        // because there are lots of paths leading to one and same result.

        //   --- Chillest Path ---
        // Finding all requested chillest paths (assuming they exist)
        HashMap<Actor, HashMap<Node, Node>> chillestPaths = new HashMap<>();
        for (Actor startActor : queries.keySet()) {
            Actor endActor = queries.get(startActor);
            HashMap<Node, Node> chillestPath = graph.getChillestPath(startActor, endActor);
            chillestPaths.put(startActor, chillestPath);
        }

        // Printing out the result (chillest paths)
        System.out.println("And Their Chillest Paths:");
        for (Actor startActor : paths.keySet()) {

            // Printing path
            HashMap<Node, Node> chillestPath = chillestPaths.get(startActor);
            String pathString = Graph.chillestPathToString(chillestPath, startActor);
            System.out.println(pathString + "\n");
        }

        //   --- Done ---
        System.out.println("Program Finished - Yay! :D");
        System.exit(0);

        // Answers to "Utforsk Videre"
        // A. Sorry, but I am too lazy for that, but I guess there is one
        //    I know that because you would not ask that otherwise, lol.
        // B. I think that the combination of views + rating is the way to go.
        //    Make the formula such that it converges to some maximum, like
        //    'weight(rating, views) = maxRating * (1 + 1 / (views + 1)) - rating'
        //    where in our case the constant 'maxRating = 10'. We can also adjust
        //    the steepness and such like 'views = views * steepness' and such.
        // C. Didn't quite understand the question. What is exactly meant by 'bevart'?
        //    I guess you 'must' include 0 to keep it the way it is? IDK...
    }
}