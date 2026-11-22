package assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AStar {
    private int size1D; // Amount of entries in the table[][] matrix
    private int size2D; // Amount of rows/columns of the table[][] matrix

    // Helper methods
    private int dist(int pos, int goalPos) {
        return Math.abs(pos / size2D - goalPos / size2D) + Math.abs(pos % size2D - goalPos % size2D);
        // Using the suggested (referenced Wikipedia page) taxicab distance as the estimated cost function
    }
    // Helper classes
    private class Node implements Comparable<Node> {
        private final Node parent;  // Must rebuild the path
        private final char move;  // Direction the empty tile moved to reach this node
        private final int[] state;  // Current game state
        private int totalCost;  // Reach cost + estimated cost

        // Constructor
        public Node(Node parent, int filledPos, int emptyPos) {
            this.parent = parent;
            int diff = filledPos - emptyPos;  // Move direction
            move = (diff == -1) ? 'L' : (diff == 1) ? 'R' : (diff == -size2D) ? 'U' : 'D';
            state = new int[size1D];  // Shallow copying the state
            System.arraycopy(parent.state, 0, state, 0, size1D);

            // Performing empty tile move
            int value = state[filledPos];
            state[emptyPos] = value;
            state[filledPos] = 0;

            // Updating cost values
            value--;  // Converting value into goal index
            int deltaEstimatedCost = dist(emptyPos, value) - dist(filledPos, value);  // Parent vs This cost difference
            totalCost = parent.totalCost + 1 + deltaEstimatedCost;  // Calculating the total cost
        }
        // Root node
        public Node(int[][] initState) {
            parent = null;
            move = 0;

            // Transforming table matrix into a list for fast shallow copying
            state = new int[size1D];
            for (int y = 0; y < size2D; y++) {
                System.arraycopy(initState[y], 0, state, y * size2D, size2D);
            }
            // Due to it being the initial state reach cost is free, thus total cost is just the estimated cost.
            // For the estimated cost we calculate based on taxicab distances as suggested by the given Wikipedia page
            totalCost = 0;
            for (int pos = 0; pos < size1D; pos++) {
                int goalPos = state[pos] - 1;  // Converting value to index
                if (goalPos < 0) {  // Ignoring empty tiles
                    continue;
                } totalCost += dist(pos, goalPos);  // CHANGED
            }
        }
        // Methods
        public ArrayList<Node> explore(HashSet<Node> blacklist) {
            ArrayList<Node> nodes = new ArrayList<>();
            int[] positions = new int[4];
            for (int y = 0; y < size2D; y++) {
                for (int x = 0; x < size2D; x++) {
                    int emptyPos = y * size2D + x;
                    if (state[emptyPos] != 0) {  // Checking if tile is filled
                        continue;
                    }
                    // -1 marks neighbors that are outside, also prevents wrap-around
                    positions[0] = x > 0 ? emptyPos - 1 : -1;
                    positions[1] = x < size2D - 1 ? emptyPos + 1 : -1;
                    positions[2] = y > 0 ? emptyPos - size2D : -1;
                    positions[3] = y < size2D - 1 ? emptyPos + size2D : -1;
                    for (int filledPos : positions) {  // Checking if tiles are filled
                        if (0 > filledPos || filledPos >= size1D) {
                            continue;
                        } if (state[filledPos] == 0) {  // Ignoring empty tiles
                            continue;
                        } Node node = new Node(this, filledPos, emptyPos);
                        if (blacklist.contains(node)) {  // Ignoring nodes we already visited
                            continue;
                        } nodes.add(node);
                    }
                }
            } return nodes;
        }
        // Implementations
        @Override
        public int compareTo(Node other) {
            return Integer.compare(totalCost, other.totalCost);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } if (obj instanceof Node other) {
                return Arrays.equals(state, other.state);
            } return false;
        }
        @Override
        public int hashCode() {
            return Arrays.hashCode(state);
        }
    }

    /**
     *
     * @param table initial state of the board
     * @return returns the string for the solution
     */
    public String solve(int[][] table) {
        size2D = table.length;
        size1D = size2D * size2D;

        // Goal to reach
        int[][] goalState = new int[size2D][size2D];
        for (int y = 0; y < size2D; y++) {
            for (int x = 0; x < size2D; x++) {
                int value = table[y][x];
                if (value > 0) {
                    int index = value - 1;
                    goalState[index / size2D][index % size2D] = value;
                }
            }
        } Node goalNode = new Node(goalState);

        // Initiating A* search algorithm
        PriorityQueue<Node> nodes = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();
        nodes.offer(new Node(table));
        while (!nodes.isEmpty()) {
            Node node = nodes.poll();
            if (!visited.add(node)) {  // Skipping duplicates that were queued before being visited
                continue;
            } if (node.equals(goalNode)) {  // Goal found check
                StringBuilder path = new StringBuilder();
                for (Node child = node; child.parent != null; child = child.parent) {
                    path.append(child.move);
                } return path.reverse().toString();
            } for (Node next : node.explore(visited)) {  // Exploring neighbors using A*'s cost function
                nodes.offer(next);
            }
        } return "";  // Queue ran out, the puzzle is unsolvable
    }
}