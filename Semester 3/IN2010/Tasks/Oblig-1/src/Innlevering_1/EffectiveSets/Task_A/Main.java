package Innlevering_1.EffectiveSets.Task_A;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Setup
        BinarySearchTree binarySearchTree = new BinarySearchTree();
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        // Continuous input stream implementation
        String input;
        while (true) {

            // Getting user input
            input = scanner.next();
            input = input.toLowerCase();

            // Evaluating input
            try {
                if (input.startsWith("exit") || input.startsWith("quit")) {
                    System.exit(0);
                }

                // These are the commands that the trees recognise
                else if (input.startsWith("contains ")) {                              // Here is a structured breakdown:
                    System.out.println(                                                // Lastly, printing the result
                            binarySearchTree.contains(                                 // Performing a method based on command
                                    Integer.parseInt(input.substring(9))));  // Getting the right values for method
                }

                // Same as "contains" part, just no need to print it out
                else if (input.startsWith("insert ")) {
                    binarySearchTree.insert(Integer.parseInt(input.substring(7)));
                }

                // Same as "insert" part
                else if (input.startsWith("remove ")) {
                    binarySearchTree.remove(Integer.parseInt(input.substring(7)));
                }

                // Same as "contains" part, just no need to get any values from input for method
                else if (input.startsWith("size")) {
                    System.out.println(binarySearchTree.size());
                }

                // Ignoring input if it is purely an integer (exception for troubleshooting)
                else {
                    try {
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    // Troubleshooting
                    System.out.println("Invalid command!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid command!");
            }

            // P.S. 1
            // If you copy-paste a bunch of commands at once
            // make sure that you also copy-paste with an extra
            // empty row at the end so that the previous line
            // ends with "\n" so that the last command will run.

            // P.S. 2
            // I am not sure if I needed to add a way to read from
            // file, but I did anyway. You can just copy-paste the
            // inputs and outputs to their corresponding files or
            // simply rename the files you already got.
        }
    }
}