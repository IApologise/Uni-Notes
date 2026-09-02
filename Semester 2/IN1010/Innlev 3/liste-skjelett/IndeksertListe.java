class IndeksertListe <E> extends Lenkeliste<E> {

    // Before you look at leggTil testing note that I coded Lenkeliste incorrecntly (apparently)
    // That is why I am getting errors now...

    public void leggTil (int pos, E x) {
        Node newNode = new Node(x);       // Creating new node
        Node iteration = this.firstNode;  // Start node

        // Checking if first is not a null and validating pos input
        if ((iteration == null & pos != 0) || pos < 0) {
            throw new UgyldigListeindeks(pos);
        }

        // Handling start
        if (pos == 0) {
            newNode.next = this.firstNode;
            this.firstNode = newNode;
            return;  // Skipping the rest
        }

        // Getting to specified node (but previous of it so that we can chain)
        for (int i = 1; i < pos; i++) {

            // Next iteration
            iteration = iteration.next;

            // Error handling
            if (iteration == null) {
                throw new UgyldigListeindeks(pos);
            }
        }

        // Adding element to the end if that is the case
        if (iteration.next == null) {
            iteration.next = newNode;
            return;  // Skipping the rest
        }

        // If adding happens anywhere in between in the list
        newNode.next = iteration.next;  // Adding the rest of the nodes to a new node
        iteration.next = newNode;       // Adding new node as previous node's next
    }

    public void sett (int pos, E x) {
        Node newNode = new Node(x);       // Creating new node
        Node iteration = this.firstNode;  // Start node

        // Checking if first is not a null and validating pos input
        if ((iteration == null & pos != 0) || pos < 0) {
            throw new UgyldigListeindeks(pos);
        }

        // Handling start
        if (pos == 0) {

            // Null as start
            if (this.firstNode == null) {
                this.firstNode = newNode;
                return;  // Skipping the rest
            }

            // Chains
            newNode.next = this.firstNode.next;  // Skipping first because we replace it
            this.firstNode = newNode;
            return;  // Skipping the rest
        }

        // Getting to specified node (but previous of it so that we can chain)
        for (int i = 1; i < pos; i++) {

            // Next iteration
            iteration = iteration.next;

            // Error handling
            if (iteration == null) {
                throw new UgyldigListeindeks(pos);
            }
        }

        // Adding element to the end if that is the case
        if (iteration.next == null) {
            // iteration.next = newNode;
            // return;  // Skipping the rest
            throw new UgyldigListeindeks(pos);  // Actually no since apparently that is not allowed...
        }

        // If adding happens anywhere in between in the list
        newNode.next = iteration.next.next;  // Adding the rest of the nodes except for the one we are replacing to a new node
        iteration.next = newNode;            // Adding new node as previous node's next
    }

    public E hent (int pos) {
	    Node iteration = this.firstNode;  // Start node

        // Validating pos and start node
        if (pos < 0 || iteration == null) {
            throw new UgyldigListeindeks(pos);
        }

        // Getting to specified node
        for (int i = 0; i < pos; i++) {
            iteration = iteration.next;  // Next node

            // Checking if valid
            if (iteration == null) {
                throw new UgyldigListeindeks(pos);
            }
        }

        // Returning element
        return iteration.element;
    }

    public E fjern (int pos) {
        Node iteration = this.firstNode;  // Start node

        // Validating start node and pos
        if (this.firstNode == null || pos < 0) {
            throw new UgyldigListeindeks(pos);  // Error handling
        }

        // Handling start
        if (pos == 0) {
            E deleted = this.firstNode.element;    // Saving value for return
            this.firstNode = this.firstNode.next;  // Setting next node as first now
            return deleted;                        // Returning deleted node's elements and skipping the rest
        }

        // Going to the specified node
        for (int i = 1; i < pos; i++) {
            iteration = iteration.next;  // Next node

            // Checking if valid
            if (iteration == null) {
                throw new UgyldigListeindeks(pos);
            }
        }

        // Checking if the element is already deleted
        if (iteration.next == null) {
            return null;  // If so, we return null, meaning -> nothing was deleted
        }

        System.out.println();
        System.out.println(iteration.element);
        System.out.println(iteration.next.element);
        if (iteration.next.next != null) {
            System.out.println(iteration.next.next.element);
        }

        E deleted = iteration.next.element;    // Saving node's element which we plan to delete from the list
        iteration.next = iteration.next.next;  // Removing the node (Also, is there a problem with garbage disposal in Java like... ever?)
        return deleted;                        // Returning deleted node's elements
    }
}
