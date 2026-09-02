class Kø <E> extends Lenkeliste<E> {

    public Node lastNode;  // No loops >:(

    @Override
    public void leggTil(E x) {
        Node newNode = new Node(x);  // New node

        if (this.firstNode == null) {  // If empty -> Make a starting node
            this.firstNode = newNode;
            this.lastNode = this.firstNode;
            return;  // So that we ignore next step
        }

        this.lastNode.next = newNode;   // Adding newNode to next of now previously last node
        this.lastNode = lastNode.next;  // Setting added node as the new last node
    }
}
