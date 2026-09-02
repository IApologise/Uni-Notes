package Graph;

public class Step {

    // Variables
    private final Actor destination;
    private final Movie medium;
    private final float weight;

    // Constructor
    public Step(Actor toActor, Movie throughMovie) {
        destination = toActor;
        medium = throughMovie;

        // Assuming that movie ratings are not above the max rating
        weight = Movie.maxRating - medium.Rating();
    }

    // Methods
    public Actor getActor() {
        return destination;
    }
    public Movie getMovie() {
        return medium;
    }
    public float getWeight() {
        return weight;
    }
}
