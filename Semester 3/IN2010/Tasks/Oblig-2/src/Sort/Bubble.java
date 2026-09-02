package Sort;

public class Bubble extends Sort {

    // Constructor
    public Bubble(int[] array) {
        super(array);
        name = "Bubble";
    }

    // Overrides
    @Override
    public int[] sort(int[] array) {
        comparisons++;  // Counting comparisons from a for loop
        for (int i = 0; i < array.length - 1; i++) {  // Checking and switching places with neighbours when needed
            comparisons++;

            comparisons++;  // Counting comparisons again
            for (int j = 0; j < array.length - i - 1; j++) {  // We do this multiple times until we are sure
                comparisons++;

                comparisons++;  // Counting comparisons from an if statement
                if (array[j + 1] < array[j]) {

                    // Switching places
                    switches += 3;  // Counting switches, necessary temporary values do count, unnecessary don't
                    int temporary = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temporary;
                }
            }
        } return array;  // Returning result

        // Question: In our case we have an overseer that tells indexes in the list to switch
        // which is slow and does take O(n^2) of overseer's time to sort things out.

        // But, what if each index would be an object and would be able to check for itself whether its
        // neighbour is larger/smaller than it is (Multi-threading)? That way the whole thing would be
        // sorted in O(n) time, no? And if it is a tree, then O(log(n))?

        // Like... imagine you are a human (crazy I know) where you and your friends must arrange
        // themselves in a line based on height. What you and your friends would do is simply look
        // at each other and move without the need of guidance from someone else. And this comparison
        // happens to all of your friends at the same time, so you are sorting all indexes simultaneously.
        // A perk of having one pointer per index instead of just one.
    }
    @Override
    public String getName() {
        return name.toLowerCase();
    }
}
