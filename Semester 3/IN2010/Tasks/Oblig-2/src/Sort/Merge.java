package Sort;

import java.util.Arrays;

public class Merge extends Sort {

    // Constructor
    public Merge(int[] array) {
        super(array);
        name = "Merge";
    }

    // Overrides
    @Override
    public int[] sort(int[] array) {
        comparisons++;
        if (array.length <= 1) {
            return array;  // Nothing to split, return
        } else {

            // Splitting the array and sorting those.
            int i = array.length / 2;  // Also, this doesn't count to switches since it was unnecessary
            switches += array.length;  // These below do count since they are arrays of temporary variables
            int[] array1 = sort(Arrays.copyOfRange(array, 0, i));
            int[] array2 = sort(Arrays.copyOfRange(array, i, array.length));

            // Returning result
            return merge(array1, array2, array);
        }
    }
    @Override
    public String getName() {
        return name.toLowerCase();
    }

    // Helper Methods
    private int[] merge(int[] array1, int[] array2, int[] array) {
        int i = 0; int j = 0;

        // Kind of like a simplified scenario for a selection sort.
        // Just go through an array B and insert the values straight
        // into the right spot inside array A which is easier now that
        // both array A and B are sorted already.

        comparisons++;
        while (i < array1.length && j < array2.length) {
            comparisons++;

            comparisons++;
            if (array1[i] <= array2[j]) {

                switches++;
                array[i + j] = array1[i];
                i++;

            } else {

                switches++;
                array[i + j] = array2[j];
                j++;
            }
        }

        comparisons++;
        while (i < array1.length) {
            comparisons++;

            switches++;
            array[i + j] = array1[i];
            i++;
        }

        comparisons++;
        while (j < array2.length) {
            comparisons++;

            switches++;
            array[i + j] = array2[j];
            j++;
        }

        // Returning result
        return array;
    }
}
