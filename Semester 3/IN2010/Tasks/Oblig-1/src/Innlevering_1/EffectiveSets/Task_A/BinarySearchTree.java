package Innlevering_1.EffectiveSets.Task_A;

// Not necessarily balanced by the way
public class BinarySearchTree {

    // Variables
    private final BinarySubTree head;
    private int count;

    // Constructor
    public BinarySearchTree() {
        head = new BinarySubTree(0);  // A quick hack, you will see how it develops later, he-he >:)
        count = 0;
    }

    // Methods
    public boolean contains(int value) {
        head.value = value + 1;
        return head.contains(value);
    }
    public void insert(int value) {
        BinarySubTree newTree = new BinarySubTree(value);  // Creating new subtree to save values and future-proofing
        head.value = value + 1;                            // "value + 1" for one less "if" to check

        // Inserting tree
        try {
            head.insert(newTree);
        } catch (ArrayStoreException e) {  // Could have created my own exception though
            return;
        } count++;  // Keeping track
    }
    public void remove(int value) {
        // We assume that our search tree contains the value
        head.value = value + 1;

        // Zooming in
        head.remove(value);
        count--;  // Keeping track
    }
    public int size() {
        return count;
    }
}
