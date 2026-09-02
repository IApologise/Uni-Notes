package Innlevering_1.EffectiveSets.Task_A;

// Helper class, more convenient
public class BinarySubTree {

    // Variables
    protected int value;  // Protected for a hack
    private BinarySubTree lesserChild;
    private BinarySubTree greaterChild;

    // Constructor
    public BinarySubTree(int value) {
        this.value = value;  // Must be defined with a value, otherwise can not compare
    }

    // Helper methods
    private BinarySubTree leastChild() {   // This method is not really used, but can be.
        if (lesserChild != null) {         // I just had to add it to ease my mind
            return lesserChild.leastChild();
        } return this;
    }
    private BinarySubTree greatestChild() {
        if (greaterChild != null) {
            return greaterChild.greatestChild();
        } return this;
    }

    // Methods
    public boolean contains(int value) {

        // Jackpot
        if (this.value == value) {
            return true;
        }

        // Zooming in further
        try {
            if (value < this.value) {
                return lesserChild.contains(value);
            } else {
                return greaterChild.contains(value);
            }
        }

        // Jackpot too
        catch (NullPointerException e) {
            return false;
        }
    }
    public void insert(BinarySubTree tree) {

        // Comparing to see which side it should end up at
        if (tree.value < value) {
            if (lesserChild == null) {  // Checking if space is occupied already
                lesserChild = tree;
            } else {
                lesserChild.insert(tree);  // Zooming in further
            }
        }

        // Duplicate, but for the other side
        else if (tree.value > value) {
            if (greaterChild == null) {  // Checking if space is occupied already
                greaterChild = tree;
            } else {
                greaterChild.insert(tree);  // Zooming in further
            }
        }

        // If the values are equal, we do nothing because we do not allow duplicates
        else {
            throw new ArrayStoreException();
        }
    }
    public void remove(int value) {
        // We assume that our search tree contains the value

        // Checking children
        if (value < this.value) {

            // Replacing lesser child if necessary
            if (value == lesserChild.value) {
                if (lesserChild.lesserChild != null) {
                    if (lesserChild.greaterChild != null) {  // Substitution case #1
                        lesserChild.lesserChild.greatestChild().greaterChild = lesserChild.greaterChild;  // Magic
                    } lesserChild = lesserChild.lesserChild;
                } else if (lesserChild.greaterChild != null) {  // Substitution case #2
                    lesserChild = lesserChild.greaterChild;
                } else {  // Substitution case #3
                    lesserChild = null;
                } return;
            }

            // Target not found, continuing search
            lesserChild.remove(value);
        }

        // Replacing greater child if necessary (copy-pasted with minor changes)
        else if (value == greaterChild.value) {
            if (greaterChild.lesserChild != null) {
                if (greaterChild.greaterChild != null) {
                    greaterChild.lesserChild.greatestChild().greaterChild = greaterChild.greaterChild;  // Magic too
                } greaterChild = greaterChild.lesserChild;
            } else if (greaterChild.greaterChild != null) {
                greaterChild = greaterChild.greaterChild;
            } else {
                greaterChild = null;
            }
        }

        // Target not found, continuing search
        else {
            greaterChild.remove(value);
        }

        // P.S.
        // The way we remove a node from a tree is described
        // incorrectly in the presentation of week 2, at 1:09:40.
        // There, you can't substitute X with Y. Although Y is
        // the least child of T3 (the greater child of X), Y may
        // have a greater child Z. In which case, if Y were to
        // substitute X, then We have a hole at Y's previous
        // position which we must substitute with back with Z.

        // I don't know if I explained the problem clearly.
        // Much easier to explain by showing it on a drawing.
        // But I am not sure if I can send one in.
        // I have one that both helps explain my method,
        // and one that helps explain the error visually.

        // Anyway, though this approach slows down tree's unbalancing,
        // It requires more checks and substitutions unless we have
        // access to the parent. As a result it complicates everything
        // substantially. In the end, the tree will lose its balance,
        // which is why I don't bother. It will end up as a linked list.

        // I hate how inefficient it is overall though. This code is
        // ugly, and I wish I would have found a more elegant solution.
        // If only I could have written "this = null;" and could have
        // done more shenanigans & improvements along the way.
    }
}