abstract class Lenkeliste <E> implements Liste<E> {

    public Node firstNode;  // First node

    protected class Node {
        public E element;  // Node's element
        public Node next;  // Reference to next node

        public Node(E element) {
            this.element = element;  // Node's element
            this.next = null;        // Reference to next node
        }
    }

    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder();  // This is more memory efficient in the long run
        Node iteration = this.firstNode;                    // Getting first node

        if (iteration != null) {                                  // If empty, no square brackets
            stringBuilder.append("[").append(iteration.element);  // This is more memory efficient too
            while (iteration.next != null) {
                iteration = iteration.next;                            // Next node
                stringBuilder.append(", ").append(iteration.element);  // Adding next element to string
            }
            stringBuilder.append("]");  // Closing string with square bracket
        }

        return stringBuilder.toString();  // Retuning string

        // Reason why it is more memory efficient:
        // We do not need to create a new String object each time we append.
        // But we need do need to do that with a "+".
    }

    @Override
    public int størrelse () {
        int count = 0;                    // Counter
        Node iteration = this.firstNode;  // First node

	    while (iteration != null) {  // If no more elements -> Terminate
            count++;
            iteration = iteration.next;  // Next node
        }

        return count;  // Returning count
    }

    @Override
    public void leggTil (E x) {
        Node newNode = new Node(x);  // Creating new node

        if (this.firstNode == null) {  // If empty -> Make a starting node
            this.firstNode = newNode;
            return;  // So that we ignore next step
        }

        newNode.next = this.firstNode;  // Making first node to be the next of the new node
        this.firstNode = newNode;       // Making new node the first
    }
    
    @Override
    public E hent () {
        if (this.firstNode == null) {  // If there is no node -> null
            return null;
        }

	    return this.firstNode.element;  // Returning the element of the first node
    }

    @Override
    public E fjern () {
        if (this.firstNode == null) {  // If there is no node -> Null
            throw new UgyldigListeindeks(0);
        }

        E deleted = this.firstNode.element;    // Saving node's element which we plan to delete from the list
        this.firstNode = this.firstNode.next;  // Removing the node
        return deleted;                        // Returning deleted node's contents
    }
}
