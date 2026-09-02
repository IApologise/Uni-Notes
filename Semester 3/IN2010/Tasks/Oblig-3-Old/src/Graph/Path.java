package Graph;

import java.util.HashMap;
import java.util.Set;

public class Path {

    // Variables
    private final HashMap<Actor, Step> path;
    private float weight;

    // Constructors
    public Path(Path path) {
        this.path = new HashMap<>(path.path);
        weight = path.weight;
    }
    public Path(HashMap<Actor, Step> path) {
        this.path = new HashMap<>();
        weight = 0;
        for (Actor actor : path.keySet()) {
            put(actor, path.get(actor));
        }
    }
    public Path(Actor actor, Step step) {
        this.path = new HashMap<>();
        weight = 0;
        put(actor, step);
    }
    public Path() {
        path = new HashMap<>();
        weight = 0;
    }

    // Methods
    public void put(Actor from, Step to) {
        path.put(from, to);
        weight += to.getWeight();
    }
    public Step get(Actor from) {
        return path.get(from);
    }
    public Set<Actor> keySet() {
        return path.keySet();
    }
    public float weight() {
        return weight;
    }
}
