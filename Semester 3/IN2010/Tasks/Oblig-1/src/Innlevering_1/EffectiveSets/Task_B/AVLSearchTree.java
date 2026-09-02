package Innlevering_1.EffectiveSets.Task_B;

// Lots of stuff is similar to BinarySearchTree.
// So I won't explain the copy-pasted code
public class AVLSearchTree {

    // Variables
    private AVLSubTree head;
    private int count;

    // Constructor
    public AVLSearchTree() {
        head = null;
        count = 0;
    }

    // Methods
    public boolean contains(int value) {
        if (head == null) {  // Base case
            return false;
        } return head.contains(value);  // Recursive case
    }
    public void insert(int value) {
        AVLSubTree newTree = new AVLSubTree(value);  // Creating new subtree to save values and future-proofing

        // Special case
        if (head == null) {
            head = newTree;  // Easy insert
            count++;         // Keeping track
            return;
        }

        // Inserting tree
        try {
            head.insert(newTree);
            head = head.grandestParent();  // Correcting head pointer
        } catch (ArrayStoreException e) {  // Could have created my own exception though
            return;
        } count++;  // Keeping track
    }
    public void remove(int value) {
        // We assume that our search tree contains the value

        // Zooming in
        head.remove(value);
        head = head.grandestParent();
        count--;  // Keeping track
    }
    public int size() {
        return count;
    }

    public void print() {
        if (head != null) {
            head.print();
        }
    }
}
