public class Verden {

    // Variables
    Rutenett rutenett;
    int genNr;

    // Constructor
    Verden(int antRader, int antKolonner) {
        this.rutenett = new Rutenett(antRader, antKolonner);  // Creating grid
        this.genNr = 0;

        this.rutenett.fyllMedTilfeldigeCeller();  // Filling with random cells
        this.rutenett.kobleAlleCeller();          // Connecting...
    }

    // Methods
    void tegn() {
        this.rutenett.tegnRutenett();  // Drawing...
        System.out.println("Generasjon: " + this.genNr);
        System.out.println("Antall levende celler: " + this.rutenett.antallLevende());
    }

    void oppdatering() {
        for (Celle[] rad : this.rutenett.rutene) {
            for (Celle celle : rad) {
                celle.tellLevendeNaboer();  // Updating each cell
                celle.oppdaterStatus();     // Including status and living neighbour amount
            }
        }
        this.genNr++;
        this.tegn();  // Drawing too...
    }
}
