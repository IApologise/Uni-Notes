package assignment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Flow {
    private final int size;  // Amount of vertices, also rows/columns
    private final int[][] capacity;  // Original capacities of edges u -> v
    private int[][] residual;  // Remaining edge's capacities
    private int[][] flow;  // Final flow over edges
    private List<Integer> cut = new ArrayList<>();  // Vertices on the cut's source side

    // Helper methods
    private int[] findPath(int source, int sink) {  // Breadth-first search (BFS)
        int[] parent = new int[size];
        Arrays.fill(parent, -1);  // -1 are vertices that aren't reached yet
        parent[source] = source;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(source);
        while (!queue.isEmpty() && parent[sink] == -1) {
            int from = queue.poll();
            for (int to = 0; to < size; to++) {
                if (parent[to] != -1 || residual[from][to] <= 0) {  // Skipping visited vertices & full edges
                    continue;
                } parent[to] = from;
                queue.add(to);
            }
        } return parent;
    }
    private int bottleneck(int[] parent, int source, int sink) {
        int amount = Integer.MAX_VALUE;
        for (int to = sink; to != source; to = parent[to]) {
            amount = Math.min(amount, residual[parent[to]][to]);
        } return amount;
    }
    private void augment(int[] parent, int source, int sink, int amount) {
        for (int to = sink; to != source; to = parent[to]) {
            int from = parent[to];
            residual[from][to] -= amount;
            residual[to][from] += amount;  // Later paths may undo this flow now
        }
    }
    private void print(int maxFlow) {  // I hate printing...
        StringBuilder output = new StringBuilder("Max-Flow: " + maxFlow + " Cut:");
        for (int vertex : cut) {
            output.append(' ').append(vertex);
        } output.append('\n');
        for (int[] row : flow) {
            for (int x = 0; x < size; x++) {
                output.append(row[x]).append(x < size - 1 ? " " : "\n");  // But I love confusing logic
            }
        } System.out.print(output);
    }

    /**
     * converts the array into a representation of your choice to solve it,
     * you may also choose to not alter it and just save it
     * @param array shows the max flow for each edge
     */
    public Flow(int[][] array) {
        size = array.length;
        capacity = new int[size][size];  // Shallow copying the capacities
        for (int y = 0; y < size; y++) {
            System.arraycopy(array[y], 0, capacity[y], 0, size);
        }
    }

    /**
     * solves it, alters the internal state and returns max flow
     * @return max flow
     */
    public int solve() {
        int source = 0;  // First is source
        int sink = size - 1;  // Last is sink

        // The graph starts out identical to the capacities
        residual = new int[size][size];
        for (int y = 0; y < size; y++) {
            System.arraycopy(capacity[y], 0, residual[y], 0, size);
        }
        // Initiating Edmonds-Karp
        int maxFlow = 0;
        int[] parent = findPath(source, sink);
        while (parent[sink] != -1) {
            int amount = bottleneck(parent, source, sink);
            augment(parent, source, sink, amount);
            maxFlow += amount;
            parent = findPath(source, sink);
        }
        // The final search could not reach the sink, so those it did reach form the minimum cut
        cut = new ArrayList<>();
        for (int vertex = 0; vertex < size; vertex++) {
            if (parent[vertex] != -1) {
                cut.add(vertex);
            }
        }
        // Flow over an edge is what was used of its capacity
        flow = new int[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                flow[y][x] = Math.max(0, capacity[y][x] - residual[y][x]);
            }
        } print(maxFlow);  // Printing...
        return maxFlow;  // Returning...
    }

    /**
     *
     * @return returns the cut
     */
    public List<Integer> cut() {
        return cut;
    }
}