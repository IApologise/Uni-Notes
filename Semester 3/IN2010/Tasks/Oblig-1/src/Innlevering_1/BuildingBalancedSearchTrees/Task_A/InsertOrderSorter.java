package Innlevering_1.BuildingBalancedSearchTrees.Task_A;

import java.util.*;

public class InsertOrderSorter {

    // Variables
    ArrayList<Integer> values;

    // Constructor
    public InsertOrderSorter() {
        values = new ArrayList<>();
    }

    // Methods
    public void addValue(int value) {
        values.add(value);
    }
    // Main method to generate the pattern
    public List<Integer> produceResult() {

        // Setup
        List<Integer> result = new ArrayList<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, values.size() - 1});

        // Continue to reduce by 2^n until done
        while (!queue.isEmpty()) {
            int[] range = queue.poll();
            int start = range[0], end = range[1];
            if (start > end) {
                continue;
            } int mid = (start + end) / 2;
            result.add(values.get(mid));
            queue.add(new int[]{start, mid - 1});
            queue.add(new int[]{mid + 1, end});
        }

        // Returning result
        return result;
    }
}
