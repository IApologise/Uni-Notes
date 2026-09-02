package Innlevering_1.BuildingBalancedSearchTrees.Task_B;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Setup
        InsertOrderSorter insertOrderSorter = new InsertOrderSorter();
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
                } else if (input.isBlank()) {
                    Integer[] resultValueOrder = insertOrderSorter.produceResult();
                    for (int insertValue: resultValueOrder) {
                        System.out.println(insertValue);
                    }
                } else {
                    insertOrderSorter.addValue(Integer.parseInt(input));
                }

                // Troubleshooting
            } catch (NumberFormatException e) {
                System.out.println("Invalid command!");
            }
        }
    }
}