package Innlevering_1.EffectiveSets.Task_B;

// Lots of stuff is similar to BinarySubTree.
// So I won't explain the copy-pasted code
public class AVLSubTree {

    // Variables
    private int value;
    private int height;  // Allows us to calculate the balance factor
    private AVLSubTree parent;
    private AVLSubTree lesserChild;
    private AVLSubTree greaterChild;

    // Constructor
    public AVLSubTree(int value) {
        this.value = value;  // Must be defined with a value, otherwise can not compare
        height = 1;
    }

    // Helper Methods
    protected AVLSubTree grandestParent() {
        if (parent != null) {
            return parent.grandestParent();
        } return this;
    }
    private AVLSubTree leastChild() {  // This method is not really used, but can be.
        if (lesserChild != null) {     // I just had to add it to ease my mind
            return lesserChild.leastChild();
        } return this;
    }
    private AVLSubTree greatestChild() {
        if (greaterChild != null) {
            return greaterChild.greatestChild();
        } return this;
    }
    private void updateHeight() {
        int previousHeight = height;

        // Updating balance factor & depth
        if (lesserChild != null) {
            if (greaterChild != null) {
                height = Math.max(lesserChild.height, greaterChild.height);
            } else {
                height = lesserChild.height;
            }
        } else if (greaterChild != null) {
            height = greaterChild.height;
        } else {
            height = 0;
        } height++;

        // Zooming out
        if (parent != null && height != previousHeight) {
            parent.updateHeight();
        }
    }
    private int getBalanceFactor() {
        if (lesserChild != null) {
            if (greaterChild != null) {
                return greaterChild.height - lesserChild.height;  // The main formula, the others are shortened
            } return -lesserChild.height;                         // Like here since greaterChild is missing
        } else if (greaterChild != null) {
            return greaterChild.height;
        } return 0;
    }
    private void leftRotate() {
        // We assume that the parent exists

        // Dad goes to buy some milk with his family and my son
        parent.greaterChild = lesserChild;
        if (lesserChild != null) {  // If we got one to spare that is
            lesserChild.parent = parent;
        }

        // I recognize dad as my new son
        lesserChild = parent;

        // Stealing grandparent from my dad
        if (parent.parent != null) {
            if (parent.parent.lesserChild == parent) {
                parent.parent.lesserChild = this;
            } else {
                parent.parent.greaterChild = this;
            } parent = parent.parent;
        } else {
            parent = null;
        }

        // Through grandpa, who is now my dad, I manage to find my lost dad,
        // who is now my son, and my son with him too. You know, the one that
        // betrayed me. So technically, my son is now my nephew.

        // Realizing that I have an authority over them both, because I stole a
        // dad from my dad, I force my dad, his family, and my son to return
        lesserChild.parent = this;  // Happy end :)

        // Some cleanup
        lesserChild.updateHeight();
        equalize();
    }
    private void rightRotate() {
        // Similar to leftRotate(), but less dramatic
        parent.lesserChild = greaterChild;
        if (greaterChild != null) {
            greaterChild.parent = parent;
        } greaterChild = parent;
        if (parent.parent != null) {
            if (parent.parent.lesserChild == parent) {
                parent.parent.lesserChild = this;
            } else {
                parent.parent.greaterChild = this;
            } parent = parent.parent;
        } else {
            parent = null;
        } greaterChild.parent = this;
        greaterChild.updateHeight();
        equalize();
    }
    private void equalize() {  // Base case
        equalize(getBalanceFactor());
    }
    private void equalize(int thisBalanceFactor) {  // Recursive case

        // Checking if we can equalize
        if (parent != null) {
            int parentBalanceFactor = parent.getBalanceFactor();

            // Checking if rotation is needed
            if (parentBalanceFactor > 1) {
                if (thisBalanceFactor < 0) {  // Double rotation
                    lesserChild.rightRotate();
                    parent.leftRotate();
                } else {  // Single rotation
                    leftRotate();
                }
            } else if (parentBalanceFactor < -1) {
                if (thisBalanceFactor > 0) {  // Double rotation
                    greaterChild.leftRotate();
                    parent.rightRotate();
                } else {  // Single rotation
                    rightRotate();
                }
            }

            // Zooming out
            else {
                parent.equalize(parentBalanceFactor);
            }
        }
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
    public void insert(AVLSubTree tree) {

        // Comparing to see which side it should end up at
        if (tree.value < value) {
            if (lesserChild == null) {  // Checking if space is occupied already

                // Inserting new tree
                lesserChild = tree;
                tree.parent = this;

                // Equalizing
                updateHeight();
                equalize();

            } else {
                lesserChild.insert(tree);  // Zooming in further
            }
        }

        // Duplicate, but for the other side
        else if (tree.value > value) {
            if (greaterChild == null) {  // Checking if space is occupied already

                // Inserting new tree
                greaterChild = tree;
                tree.parent = this;

                // Equalizing
                updateHeight();
                equalize();

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

        // Jackpot
        if (value == this.value) {
            AVLSubTree targetChild;
            AVLSubTree targetSibling;

            // Finding substitution
            if (lesserChild != null) {
                targetChild = lesserChild.greatestChild();
            } else if (greaterChild != null) {
                targetChild = greaterChild.leastChild();
            } else {
                targetChild = this;
            }

            // Substituting values
            this.value = targetChild.value;
            if (targetChild.parent.lesserChild == targetChild) {
                if (targetChild.parent.greaterChild != null) {
                    targetSibling = targetChild.parent.greaterChild;
                } else {
                    targetSibling = targetChild.parent;
                } targetChild.parent.lesserChild = null;
            } else {
                if (targetChild.parent.lesserChild != null) {
                    targetSibling = targetChild.parent.lesserChild;
                } else {
                    targetSibling = targetChild.parent;
                } targetChild.parent.greaterChild = null;
            }

            // Equalizing
            targetSibling.updateHeight();
            targetSibling.equalize();
        }

        // Zooming in
        else if (value < this.value) {
            lesserChild.remove(value);
        } else {
            greaterChild.remove(value);
        }
    }

    private void print(String prefix) {
        System.out.println(prefix + value);
        if (lesserChild != null) {
            lesserChild.print(prefix + "/");
        } if (greaterChild != null) {
            greaterChild.print(prefix + "\\");
        }
    }
    public void print() {
        print("|");
    }
}
