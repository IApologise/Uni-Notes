package node;

import java.util.Scanner;

public abstract class Node {

    // Common variables
    private final String ID;
    private final String name;

    // Constructor
    public Node(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    // Getter Methods
    public String getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
}
