package assignment;

import java.util.ArrayList;
import java.util.List;

public class BottomUpSos {

    // Variables
    private final int[] sequence;
    private Boolean[][] table;

    // Helper methods
    private boolean reachable(int i, int sum) {
        return sum == 0 || table[i][sum - 1];  // Sum 0 has no column, the empty selection always works
    }

    // Methods
    /**
     * 
     * @param sequence an array of positive integers 
     */
    public BottomUpSos(int[] sequence) {
        this.sequence = sequence;
        init();
    }
    /**
     * Calculates the table from here.
     * The table is n x S. Column c stands for the sum c + 1 (sums 1..S), and
     * table[i][c] is true if some subset of sequence[0..i] adds up to c + 1.
     */
    private void init() {

        // Getting sum of all elements
        int total = 0;
        for (int x : sequence) {
            total += x;
        } table = new Boolean[sequence.length][total];

        // Nothing to fill?
        if (sequence.length == 0) {
            return;  // Yup...
        }
        // First row: with only sequence[0], the only reachable sum is sequence[0] itself
        for (int sum = 1; sum <= total; sum++) {
            table[0][sum - 1] = (sum == sequence[0]);
        }
        // Remaining rows, each one only needs the row above it
        for (int i = 1; i < sequence.length; i++) {
            for (int sum = 1; sum <= total; sum++) {
                boolean skip = table[i - 1][sum - 1];  // Sum was already possible without this element
                boolean take = sum >= sequence[i] && reachable(i - 1, sum - sequence[i]);  // Use this element, rest made by earlier ones
                table[i][sum - 1] = skip || take;
            }
        }
    }
    /**
     * 
     * @param k the target sum
     * @return retuns an int array of the subsequence that can make the number k, return null if it can't be made
     */
    public int[] checkSum(int k){
        if (sequence.length == 0 || k < 1 || k > table[0].length) {  // Outside the table
            return null;
        } if (!table[sequence.length - 1][k - 1]) {  // Last row holds the answer, false means k can't be made
            return null;
        }
        // Backtracking through the table to find which elements were used
        List<Integer> result = new ArrayList<>();
        int sum = k;  // What is left to make
        for (int i = sequence.length - 1; i >= 0 && sum > 0; i--) {
            if (i > 0 && table[i - 1][sum - 1]) {
                continue;  // Reachable without sequence[i], so it was not used
            }
            result.add(sequence[i]);  // It was used, so add it & remove it from what is left
            sum -= sequence[i];
        }
        return result.stream().mapToInt(Integer::intValue).toArray();  // Returning list to int array, thanks google :)
    }
}
