import Sort.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // P.S. The way measurements are made can be noticed in the code itself and its comments.
        // Although they are explained only on first time introductions, so I will repeat it here.
        // Each comparison inside loops and if statements counts as a single comparison. If there
        // is an &&, then it counts as 2 since the same can be replicated with 2 if statements.

        // Switches are counted every time something is assigned a new value, as long as it is
        // necessary and isn't there to help keep count of an iteration of a loop for example.
        // Although that isn't quite the definition of switches, sometimes they are made indirectly
        // through complex sequences, and thus this definition simplifies it a lot.

        // I don't think my definition of switches is a great one. I could have simply not counted
        // the temporary variables, but that would be cheating since everything can be assigned to
        // a new variable this way and thus not count to switches resulting in a total of 0 for all.

        // Bad program usage check
        if (args.length < 1) {
            System.out.println("Error: Path to File Required.");
            System.out.println("Blueprint: java [Program Name] [File Path]");
            System.exit(-1);
        }

        // Reading content from file to array
        int[] array = Sort.readFromFile(args[0]);

        // Creating sorting algorithm objects
        Bubble bubbleSort = new Bubble(Arrays.copyOf(array, array.length));
        Insertion insertionSort = new Insertion(Arrays.copyOf(array, array.length));
        Merge mergeSort = new Merge(Arrays.copyOf(array, array.length));
        Quick quickSort = new Quick(Arrays.copyOf(array, array.length));

        // Registering those objects
        Sort[] sorts = new Sort[]{bubbleSort, insertionSort, mergeSort, quickSort};

        // Performing tasks
        for (Sort sort: sorts) {
            sort.measure();             // Sorting and measuring
            sort.arrayToFile(args[0]);  // Printing result array to file
        }

        // Saving data to a single csv file
        String fileName = args[0].substring(0, args[0].length() - 4) + "_results.csv";
        File file = new File(fileName);
        try {
            file.createNewFile();

            // File creation failed
        } catch (IOException e) {
            System.out.println("Failed to Create a New File.");
            System.exit(-1);
        }

        // Writing header column to a csv file
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append("n");
            for (Sort sort: sorts) {
                fileWriter.append(", ")
                        .append(sort.getName()).append("_cmp, ")
                        .append(sort.getName()).append("_swaps, ")
                        .append(sort.getName()).append("_time");
            }
            fileWriter.append("\n");

            // Looping through an array from size 0 to max and measuring results
            for (int i = 0; i <= array.length; i++) {
                fileWriter.append(Integer.toString(i));  // Writing array size to file
                for (Sort sort: sorts) {
                    sort.setArray(Arrays.copyOf(array, i));  // Changing array
                    sort.measure();                          // Sorting and measuring

                    // Writing row results to file
                    fileWriter.append(", ").append(String.valueOf(sort));
                } fileWriter.append("\n");
            }

            // Closing file
            fileWriter.close();

            // File write failed
        } catch (IOException e) {
            System.out.println("Error: Failed to Write Data to a New File.");
            System.exit(-1);
        }

        // Done :D
        System.exit(0);
    }
}