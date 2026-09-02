package Graph;

import Logic.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {

    // Variables
    private final HashMap<Movie, HashSet<Actor>> movieEdges;
    private final HashMap<Actor, HashSet<Movie>> actorEdges;
    private final HashSet<HashSet<Actor>> components;

    // Note that all the actors are in the edges as keys,
    // so there is no need for a node set (it is redundant).
    // Same goes for movies since all of them are in at least
    // one actor's movie st that they played in.

    // But, these are still useful when counting, So what we do
    // is we use them as keys for two of our edge set variations.

    // Constructor
    public Graph(HashMap<String, Movie> movies, HashMap<String, Actor> actors) {

        // Mapping in form of neighbour lists (both at once)
        actorEdges = new HashMap<>();
        movieEdges = new HashMap<>();
        for (Actor actor: actors.values()) {
            HashSet<Movie> playedIn = new HashSet<>();  // Movies actor played in (edge list)

            // Locating movies based on which ones actor played in
            for (String TTID : actor.TTIDs()) {
                Movie movie = movies.get(TTID);
                if (movie != null) {  // Ignoring TTIDs that are not in movies

                    // adding movie to the actor's edge set
                    playedIn.add(movie);

                    // Adding actor to the movie's edge set
                    if (!movieEdges.containsKey(movie)) {
                        movieEdges.put(movie, new HashSet<>());
                    } movieEdges.get(movie).add(actor);
                }
            }  // Adding actor and its edge set
            actorEdges.put(actor, playedIn);
        }

        // Getting components
        components = getComponents();
    }

    // Helper methods
    private HashSet<HashSet<Actor>> getComponents() {
        HashSet<HashSet<Actor>> components = new HashSet<>();

        // Similar (but not completely) process to helper nodes
        for (Actor actor : actorEdges.keySet()) {
            HashSet<Movie> movies = new HashSet<>();
            HashSet<Actor> actors = new HashSet<>();

            // Checking if actor is already in some component
            boolean actorInComponent = false;
            for (HashSet<Actor> component : components) {
                if (component.contains(actor)) {
                    actorInComponent = true;
                    break;
                }
            }  // Getting new component
            if (!actorInComponent) {
                actors.add(actor);

                // Keeping track of changes so that we know when component assembly is done
                HashSet<Actor> newActors = new HashSet<>(actors);
                while (!newActors.isEmpty()) {

                    // Deepening search through newly added actors
                    HashSet<Movie> newMovies = new HashSet<>();
                    for (Actor newActor : newActors) {
                        newMovies.addAll(actorEdges.get(newActor));
                    } newMovies.removeAll(movies);

                    // Deepening search through newly added movies
                    newActors = new HashSet<>();
                    for (Movie newMovie : newMovies) {
                        newActors.addAll(movieEdges.get(newMovie));
                    } newActors.removeAll(actors);

                    // Add new actors. Then, rinse and repeat
                    movies.addAll(newMovies);
                    actors.addAll(newActors);
                }
                // Finalizing component creation
                components.add(actors);
            }
        }  // Returning result
        return components;
    }
    @Deprecated
    private HashSet<HashSet<Actor>> getComponent() {

        // Setup
        HashSet<HashSet<Actor>> components = new HashSet<>();

        // Similar (but not completely) process to helper nodes
        for (Actor actor : actorEdges.keySet()) {
            HashSet<Movie> movies = new HashSet<>();
            HashSet<Actor> actors = new HashSet<>();

            // Checking if actor is already in some component
            boolean actorInComponent = false;
            for (HashSet<Actor> component : components) {
                if (component.contains(actor)) {
                    actorInComponent = true;
                    break;
                }
            }  // Getting new component
            if (!actorInComponent) {
                actors.add(actor);
                getActorEdges(actor, movies, actors);
                components.add(actors);
            }
        } // Returning output
        return components;

        // P.S. after testing with the big dataset, I got a StackOverflowError
        // Guess I will have to remake it even though it works perfectly ;(
        // Anyway, I guess ignore these 3 functions that I made. I am fine...
        // No, I am not sad ;( ;( ;(, why did you even suspect such a thing?
    }
    @Deprecated
    private void getMovieEdges(Movie movie, HashSet<Movie> movies, HashSet<Actor> actors) {

        // Exhaustion search procedure 1/2
        for (Actor actor : movieEdges.get(movie)) {
            if (!actors.contains(actor)) {
                actors.add(actor);
                getActorEdges(actor, movies, actors);
            }
        }
    }
    @Deprecated
    private void getActorEdges(Actor actor, HashSet<Movie> movies, HashSet<Actor> actors) {

        // Exhaustion search procedure 2/2
        for (Movie movie : actorEdges.get(actor)) {
            if (!movies.contains(movie)) {
                movies.add(movie);
                getMovieEdges(movie, movies, actors);
            }
        }
    }

    // Methods
    public int getNodeCount() {
        return actorEdges.keySet().size() + movieEdges.keySet().size();
    }
    public int getEdgeCount() {
        int edgesCount = 0;

        // Small prep
        Collection<HashSet<Actor>> actorsCollection = movieEdges.values();
        Collection<HashSet<Movie>> moviesCollection = actorEdges.values();

        // Choosing counting option with the least loops
        if (moviesCollection.size() < actorsCollection.size()) {
            for (HashSet<Movie> movies : moviesCollection) {
                edgesCount += movies.size();
            }
        } else {
            for (HashSet<Actor> actors : actorsCollection) {
                edgesCount += actors.size();
            }
        }  // Returning counted value
        return edgesCount;

        // I could count whilst creating those lists.
        // But, that is nothing in computing power required
        // when compared to creating those sets.
    }
    public HashMap<Integer, Integer> getComponentsCount() {

        // Counting how many are of same size
        HashMap<Integer, Integer> componentsCount = new HashMap<>();
        for (HashSet<Actor> component : components) {
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
    public HashMap<Actor, Pair<Movie, Actor>> getPath(Actor startActor, Actor endActor) {

        // Checking if it is even possible
        boolean shareSameComponent = false;
        for (HashSet<Actor> component : components) {
            if (component.contains(startActor) && component.contains(endActor)) {
                shareSameComponent = true;  // Possible :D
                break;
            }
        } if (!shareSameComponent) {
            return null;  // Not possible ;(
        }

        // Implementing width-first search
        HashMap<Actor, HashMap<Actor, Pair<Movie, Actor>>> paths = new HashMap<>();
        paths.put(startActor, new HashMap<>());

        // Logic behind it is that the key Actor is the newly added actor.
        // Its value is a HashMap where we got that new actor from. Those
        // HashMaps are like paths where Actor, Pair<Movie, Actor> represent
        // the step taken to go from Actor through a Movie to another Actor.

        // This way we automatically discard dead ends since they either
        // lead to a solution actor and we terminate, or don't result in a
        // new actor (dead end). This way we also ignore duplicate paths and
        // paths that lead to same nodes (variants). We also don't allow the
        // algorithm to go back because again, it produces no new actors.

        // Keeping track of previously visited nodes
        HashSet<Movie> visitedMovies = new HashSet<>();
        HashSet<Actor> visitedActors = new HashSet<>();

        // I could have flipped the roles and made paths a HashSet instead,
        // but this takes less memory because why store a bunch of dead ends
        // (HashSets) when I can store a single HashSet? This does not apply
        // to visitedMovies though, it is there for a different reason.

        // Searching...
        while (!paths.isEmpty()) {

            // Also, searching paths.keySet() instead of visitedActors
            // because visitedActors only gets larger and larger with time
            // whilst paths.keySet() only get smaller and smaller (duh)

            // Depth step-up
            HashMap<Actor, HashMap<Actor, Pair<Movie, Actor>>> newPaths = new HashMap<>();
            for (Actor actor : paths.keySet()) {

                // Visiting new actor's movies (only new movies)
                for (Movie newMovie : actorEdges.get(actor)) {
                    if (!visitedMovies.contains(newMovie)) {

                        // Visiting new movie's actors (only new actors)
                        for (Actor newActor : movieEdges.get(newMovie)) {
                            if (!visitedActors.contains(newActor)) {

                                // New actor, adding that variation
                                HashMap<Actor, Pair<Movie, Actor>> path = new HashMap<>(paths.get(actor));
                                path.put(actor, new Pair<>(newMovie, newActor));
                                newPaths.put(newActor, path);

                                // If target actor was found, end it here
                                if (newActor == endActor) {
                                    return path;
                                }

                                // Actor is now visited
                                visitedActors.add(newActor);
                            }
                        }  // Movie is now visited
                        visitedMovies.add(newMovie);
                    }
                }  // Previous paths are too old now
            } paths = newPaths;
        }
        // Oops, something went wrong
        return null;
    }
    public Path getChillestPath(Actor startActor, Actor endActor) {

        // Here I just recycled my algorithm I used in the getPath().
        // I ignored some comments and added new ones for new stuff.
        // I also introduced a new path object which I definitely
        // should have done before to easy things up a bit.

        // Although I think there are definitely better algorithms
        // than this one, although I am not sure about that...

        // Checking if it is even possible
        boolean shareSameComponent = false;
        for (HashSet<Actor> component : components) {
            if (component.contains(startActor) && component.contains(endActor)) {
                shareSameComponent = true;  // Possible :D
                break;
            }
        } if (!shareSameComponent) {
            return null;  // Not possible ;(
        }

        // Trackers
        HashMap<Actor, Path> paths = new HashMap<>();    // Repurposed for something else now
        HashSet<Actor> actors = new HashSet<>();         // Kind of like the original paths
        HashSet<Movie> visitedMovies = new HashSet<>();  // Removed visitedActors, check changelog

        // Changelog: We remove visitedActors because the weights are
        // based on movie ratings, and we have to account for that now.
        // The path taken to the actor now matters, and we want the best.
        // So now, we handle collisions by choosing the path with less weight.
        // Due to this, we also have to store previous path variants.
        // We also added a way to keep track of weights that our paths have.
        // And we keep track of the chillest path we found so far.

        // Small setup
        paths.put(startActor, new Path());
        actors.add(startActor);

        // Initiating algorithm
        while (!actors.isEmpty()) {

            // Depth step-up
            HashSet<Actor> newActors = new HashSet<>();
            for (Actor actor : actors) {
                Path currentPath = paths.get(actor);

                // Visiting new actor's movies (only new movies)
                for (Movie newMovie : actorEdges.get(actor)) {

                    // Visiting new movie's actors (even duplicates)
                    for (Actor newActor : movieEdges.get(newMovie)) {

                        // New actor, creating variation
                        Path path = new Path(currentPath);
                        path.put(actor, new Step(newActor, newMovie));

                        // Collision handling
                        if (paths.containsKey(newActor)) {
                            if (path.weight() >= paths.get(newActor).weight()) {
                                continue;  // We ignore worse path variants (in all cases)
                            }
                        } paths.put(newActor, path);

                        // Ignoring paths that are already worse than the solution variant
                        if (paths.containsKey(endActor)) {
                            if (path.weight() >= paths.get(endActor).weight()) {
                                    continue;
                                }
                        } newActors.add(newActor);
                    }
                }  // Previous actors are too old now
            } actors = newActors;
        }
        // Returning result
        return paths.get(endActor);

        // Also, now that I look back at the way I coded my stuff
        // it looks complicated, maybe I should have created a
        // separate Node<T> object and some sort of Edge object
        // as well as path object and such, but whatever now.

        // P.S. I did create a path object and a step, that's it.
        // Also, not much really changed to be honest, it just made
        // it easier for me to check the weights (syntax wise).

        // I also hate how slow it is, but that is good enough for me.
    }
}
