package Sort;

public class Insertion extends Sort {

    // Constructor
    public Insertion(int[] array) {
        super(array);
        name = "Insertion";
    }

    // Overrides
    @Override
    public int[] sort(int[] array) {
        comparisons++;
        for (int i = 1; i < array.length; i++) {  // j must be > 0 anyways, so we start at i = 1
            comparisons++;

            // Does this for loop count as 2 since it has && in it?
            // I will make the assumption that it does everywhere further on
            comparisons += 2;
            for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {  // Selection sorting added value to sub-array
                comparisons += 2;

                // Switching places
                switches += 3;
                int temporary = array[j - 1];
                array[j - 1] = array[j];
                array[j] = temporary;
            }
        } return array;  // Returning result
    }
    @Override
    public String getName() {
        return name.toLowerCase();
    }
}
