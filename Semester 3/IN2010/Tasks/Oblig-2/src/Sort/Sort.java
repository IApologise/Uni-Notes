package Sort;

import java.util.*;
import java.io.*;

public abstract class Sort {

    // Variables
    protected String name;
    private int[] array;
    protected int comparisons;
    protected int switches;
    protected long time;

    // Constructor
    public Sort(int[] array) {
        this.array = array;
    }

    // Required implementations
    public abstract int[] sort(int[] array);
    public abstract String getName();

    // Methods
    public void setArray(int[] array) {
        this.array = array;
    }
    public void measure() {

        // Setup
        comparisons = 0;
        switches = 0;
        long start = System.nanoTime();

        // Sorting
        this.array = sort(array);

        // Calculating
        long end = System.nanoTime();
        time = (end - start) / 1000;
    }
    public void arrayToFile(String prefix) {

        // Naming and creating new file
        String name = this.name.toLowerCase().replaceAll("\\s+", "_");
        String fileName = prefix.substring(0, prefix.length() - 4) + "_" + name + ".out";
        File file = new File(fileName);
        try {
            file.createNewFile();

            // File creation failed
        } catch (IOException e) {
            System.out.println("Failed to Create a New File.");
            System.exit(-1);
        }

        // Writing result to newly made file
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            String arrayText = Arrays.toString(array);                   // Converting array to string
            arrayText = arrayText.substring(1, arrayText.length() - 1);  // Removing square brackets
            arrayText = arrayText.replace(", ", "\n");  // Distinguishing between values
            fileWriter.write(arrayText);                                 // Writing to file
            fileWriter.close();

            // File write failed
        } catch (IOException e) {
            System.out.println("Error: Failed to Write Data to a New File.");
            System.exit(-1);
        }
    }

    // Static Methods
    public static int[] readFromFile(String filePath) {
        LinkedList<Integer> list = new LinkedList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {

            // Reading file contents
            while (scanner.hasNext()) {
                list.add(Integer.parseInt(scanner.nextLine()));  // Adding line to array
            }

            // Casting from LinkedList<Integer> to int[]
            int arrayListSize = list.size();
            int[] newArray = new int[arrayListSize];
            for (int i = 0; i < arrayListSize; i++) {
                newArray[i] = list.getFirst();
                list.removeFirst();
            }

            // Returning result
            return newArray;

            // File not found error
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Not Found.");
            System.exit(-1);
        } return null;
    }

    // Overrides
    @Override
    public String toString() {
        return comparisons + ", " + switches + ", " + time;
    }
}
