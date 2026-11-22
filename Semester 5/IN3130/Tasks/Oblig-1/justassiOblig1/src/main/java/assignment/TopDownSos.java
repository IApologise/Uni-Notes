package assignment;

import java.util.ArrayList;
import java.util.List;

public class TopDownSos {

    // Variables
    private final int[] sequence;
    private Boolean[][] table;

    // Helper methods
    private boolean solve(int i, int sum) {
        if (sum == 0) {
            return true;  // Sum 0 has no column so the empty selection always works
        } if (sum < 0) {
            return false;  // Element was too big to be used
        } if (table[i][sum - 1] != null) {
            return table[i][sum - 1];  // Already computed so reuse it
        }
        // Either skip this element, or use it & make the rest with earlier ones
        boolean result = solve(i - 1, sum) || solve(i - 1, sum - sequence[i]);
        table[i][sum - 1] = result;  // Saving it for later...
        return result;
    }

    // Methods
    /**
     * 
     * @param sequence an array of positive integers 
     */
    public TopDownSos(int[] sequence) {
        this.sequence = sequence;
        init();
    }
    /**
     * fills the first column and row
     * The table is n x S. Column c stands for the sum c + 1 (sums 1..S), and
     * table[i][c] is true if some subset of sequence[0..i] adds up to c + 1.
     */
    private void init(){
        // Getting sum of all elements
        int total = 0;
        for (int x : sequence) {
            total += x;
        } table = new Boolean[sequence.length][total];

        // Nothing to fill?
        if (sequence.length == 0) {
            return;  // Yep...
        }
        // First row: with only sequence[0], the only reachable sum is sequence[0] itself
        for (int sum = 1; sum <= total; sum++) {
            table[0][sum - 1] = (sum == sequence[0]);
        }
        // First column, i.e. sum 1, is reachable as soon as one of the elements so far is 1
        for (int i = 1; i < sequence.length; i++) {
            table[i][0] = table[i - 1][0] || sequence[i] == 1;
        }
    }
    /**
     * Call a recursive function that fills in the parts necessarily parts of the table
     * @param k the target sum 
     * @return retuns an int array of the subsequence that can make the number k, return null if it can't be made
     */
    public int[] checkSum(int k){
        if (sequence.length == 0 || k < 1 || k > table[0].length) {
            return null;  // Outside the table
        } if (!solve(sequence.length - 1, k)) {
            return null;  // Only fills what k needs, false means k can't be made
        }
        // Backtracking to find which elements were used
        List<Integer> result = new ArrayList<>();
        int sum = k;  // What is left to make
        for (int i = sequence.length - 1; i >= 0 && sum > 0; i--) {
            if (i > 0 && solve(i - 1, sum)) {
                continue;  // Reachable without sequence[i], so it was not used
            }
            result.add(sequence[i]);  // It was used, so add it & remove it from what is left
            sum -= sequence[i];
        }
        return result.stream().mapToInt(Integer::intValue).toArray();  // Returning list to int array, thanks google :)
    }
}
