package assignment;

import java.util.TreeMap;

class Trie {

    // Helper classes
    private static class Node {
        String label;  // Part of a word that is stored
        boolean isWord;  // True if it is an actual word that was inserted
        TreeMap<Character, Node> children = new TreeMap<>(); // Sorted alphabetically

        // Constructor
        Node(String label, boolean isWord) {
            this.label = label;
            this.isWord = isWord;
        }
    }

    // Variables
    private final Node root = new Node("", false);

    // Helper methods
    private String print(Node node) {
        StringBuilder stringBuilder = new StringBuilder("(").append(node.label);  // Parentheses start & this prefix
        for (Node child : node.children.values()) {
            stringBuilder.append(print(child));  // Recursive print for each child
        } return stringBuilder.append(")").toString();  // Closing parentheses
    }
    private static int commonPrefixLength(String a, String b) {
        int n = Math.min(a.length(), b.length());  // Max length we can check
        int i = 0;
        while (i < n && a.charAt(i) == b.charAt(i)) {  // Finding mismatch
            i++;
        } return i;  // Returning prefix length
    }

    // Methods
    /**
     * This function inserts a word in to the trie
     * @param word you can assume the word is lowercase
     */
    public void insert(String word) {
        Node node = root;  // Starting with root note
        String remaining = word;  // Keeping track of remaining parts

        // Navigating nodes & placing words parts as we go deeper
        while (true) {
            if (remaining.isEmpty()) {  // No parts left?
                node.isWord = true;  // Then set as word & return
                return;
            }

            // Checking if this is the last part to be filled
            Node child = node.children.get(remaining.charAt(0));  // Setup for future too
            if (child == null) {
                node.children.put(remaining.charAt(0), new Node(remaining, true));
                return;
            }
            // Getting common prefix between remaining & child
            int common = commonPrefixLength(child.label, remaining);
            if (common < child.label.length()) {

                // Need to split the child, middle holds the shared prefix, child keeps the rest
                Node middle = new Node(child.label.substring(0, common), false);  // Creating middle
                child.label = child.label.substring(common);  // What child keeps
                middle.children.put(child.label.charAt(0), child);  // What middle keeps
                node.children.put(middle.label.charAt(0), middle);  // Adding middle to parent
                child = middle;
            }
            // Next iteration setup
            node = child;
            remaining = remaining.substring(common);
        }
    }
    /**
     * This function checks if a word has been inserted into the trie
     * @param word you can assume it's always lowercase
     * @return returs true if the word is found, returns flase if the word is not found, if hight is inserted and the function recives high it should return false, only inserted words should give true
     */
    public Boolean lookUp(String word) {
        Node node = root;
        String remaining = word;

        // Traversing the tree
        while (!remaining.isEmpty()) {
            Node child = node.children.get(remaining.charAt(0));
            if (child == null || !remaining.startsWith(child.label)) {
                return false;  // Not found, false
            }
            // Going deeper
            remaining = remaining.substring(child.label.length());
            node = child;
        } return node.isWord;  // Returning true if word, else false
    }
    /**
     * This function prints the trie in alphabetical order
     * example:
     * internet: (internet)
     * interview: (inter(net)(view))
     * inter: (inter(net)(view))
     * @return retuns a string that shows the state of the trie
     */
    public String printTree() {
        if (root.children.size() == 1) {  // Root with one child prints as if a child
            return print(root.children.firstEntry().getValue());
        } return print(root);  // Otherwise normal behaviour
    }
}
