package node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Movie extends Node {

    // Static variables
    public static final float maxRating = 10;

    // Unique variables
    private final float rating;

    // Constructor
    public Movie(String TTID, String name, float rating) {
        super(TTID, name);
        this.rating = rating;
    }

    // Static Methods
    public static HashMap<String, Movie> getMovies(String filePath) {
        HashMap<String, Movie> movies = new HashMap<>();

        // Opening and reading file
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNext()) {

                // Identifying and formatting inputs
                String[] inputs = scanner.nextLine().split("\t");

                // Creating and adding actor to list
                Movie movie = new Movie(inputs[0], inputs[1], Float.parseFloat(inputs[2]));
                movies.put(inputs[0], movie);
            }

            // Error, file is missing
        } catch (FileNotFoundException e) {
            System.out.println("Error, '" + filePath + "' is missing!");
            System.exit(-1);
        }

        // Returning movies list
        return movies;
    }

    // Getter Methods
    public float getRating() {
        return rating;
    }
}
