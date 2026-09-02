package node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Actor extends Node {

    // Unique variables
    private final String[] TTIDs;

    // Constructor
    public Actor (String NMID, String name, String... TTIDs) {
        super(NMID, name);
        this.TTIDs = TTIDs;
    }

    // Static Methods
    public static HashMap<String, Actor> getActors(String filePath) {
        HashMap<String, Actor> actors = new HashMap<>();

        // Opening and reading file
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNext()) {

                // Identifying and formatting inputs
                String[] inputs = scanner.nextLine().split("\t");
                String input0 = inputs[0]; String input1 = inputs[1];
                inputs = Arrays.copyOfRange(inputs, 2, inputs.length);

                // Creating and adding actor to list
                Actor actor = new Actor(input0, input1, inputs);
                actors.put(input0, actor);
            }

            // Error, file is missing
        } catch (FileNotFoundException e) {
            System.out.println("Error, '" + filePath + "' is missing!");
            System.exit(-1);
        }

        // Returning actors list
        return actors;
    }

    // Getter Methods
    public String[] getTTIDs() {
        return TTIDs;
    }
}
