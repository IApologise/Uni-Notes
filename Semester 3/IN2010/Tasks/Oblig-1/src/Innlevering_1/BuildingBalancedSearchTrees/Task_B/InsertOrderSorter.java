package Innlevering_1.BuildingBalancedSearchTrees.Task_B;

import java.util.ArrayList;

// Here we are only allowed to use priority queues to do our bidding
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
    public Integer[] produceResult() {
        Integer[] result = new Integer[values.size()];
        return result;
    }
}
