package Sort;

import java.util.Random;

public class Quick extends Sort {

    // Variables
    private final Random random;

    // Constructors
    public Quick(int[] array, long seed) {
        super(array);
        name = "Quick";
        random = new Random(seed);  // Got to stay consistent to simplify testing and bug fixing
    }
    public Quick(int[] array) {
        this(array, 42);  // Google "what is the answer to life the universe and everything" to find out more
    }

    // Overrides
    @Override
    public int[] sort(int[] array) {
        return Quicksort(array, 0, array.length - 1);  // Initial state to start sorting properly
    }
    @Override
    public String getName() {
        return name.toLowerCase();
    }

    // Helper Methods
    private int ChoosePivot(int low, int high) {
        return random.nextInt(1 + high - low) + low;  // Pseudo-random integer generator
    }
    private int Partition(int[] array, int low, int high) {
        int temporary;

        // Gambling on sorting efficiency
        int pivotIndex = ChoosePivot(low, high);

        // Switching pivot with last element for simplicity
        switches += 3;
        temporary = array[pivotIndex];
        array[pivotIndex] = array[high];
        array[high] = temporary;

        // Setup
        // These are pointers, so they don't count as switches
        int pivot = array[high];
        int left = low;
        int right = high - 1;

        // Finding culprit pairs (bigger than pivot on left side and smaller on right)
        comparisons++;
        while (left <= right) {
            comparisons++;

            comparisons += 2;
            while (left <= right && array[left] <= pivot) {
                comparisons += 2;

                left++;
            }

            comparisons += 2;
            while (right >= left && array[right] >= pivot) {
                comparisons += 2;

                right--;
            }

            comparisons++;
            if (left < right) {

                // Imbalance found, switching places
                switches += 3;
                temporary = array[left];
                array[left] = array[right];
                array[right] = temporary;
            }
        }

        // Placing the pivot back via switching (and with the right one at that)
        switches += 3;
        temporary = array[left];
        array[left] = array[high];
        array[high] = temporary;

        // Wrapping up
        return left;
    }
    private int[] Quicksort(int[] array, int low, int high) {

        // Almost like merge sort, but in merge sort we split by
        // indexes, here we split by values with the pivot instead.
        if (high >= low) {
            int pivotIndex = Partition(array, low, high);  // This is unnecessary, so it doesn't count towards switches
            Quicksort(array, low, pivotIndex - 1);
            Quicksort(array, pivotIndex + 1, high);
        } return array;
    }
}
