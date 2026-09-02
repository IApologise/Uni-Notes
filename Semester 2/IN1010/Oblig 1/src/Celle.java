import java.util.ArrayList;

public class Celle {

    // Variables
    boolean levende;
    ArrayList<Celle> naboer;
    int antNaboer;
    int antLevendeNaboer;

    // Honestly I wanted those to be private, but the TestCelle doesn't think it should be :/
    // Like... Who am I making erLevende() for? Honestly just use celle.levende at this point.
    // This is why I left all those public. However, I would rather use private on the variables
    // that I do not want to be tampered with.

    // Constructor
    Celle(){
        this.levende = false;
        this.naboer = new ArrayList<>(8);  // Creating an array the size of 8
        this.antNaboer = 0;
        this.antLevendeNaboer = 0;
    }

    // Methods
    void settLevende(){
        this.levende = true;
    }

    void settDød(){
        this.levende = false;
    }

    boolean erLevende(){
        return this.levende;
    }

    char hentStatusTegn(){
        if (this.erLevende()){  // Checking if alive
            return 'O';
        } else {
            return '.';
        }
    }

    void leggTilNabo(Celle nabo){
        this.naboer.add(nabo);  // Adding neighbour to the list
        this.antNaboer++;       // Increasing neighbour count
    }

    void tellLevendeNaboer(){
        int antLevendeNaboer = 0;  // Declaration
        for (Celle nabo : this.naboer){
            if (nabo.erLevende()){
                antLevendeNaboer++;  // For each neighbour that is alive -> Increment by one
            }
        }
        this.antLevendeNaboer = antLevendeNaboer;  // Save
    }

    void oppdaterStatus(){
        if (this.erLevende()){
            if (antLevendeNaboer < 2 || antLevendeNaboer > 3) {  // If alive and: less than 2 or more than 3...
                this.settDød();                                  // >>> Set current to Dead
            }
        } else if (antLevendeNaboer == 3){  // If dead and: exactly 3 neighbours are alive...
            this.settLevende();             // >>> Set current to Alive
        }
    }
}
