public abstract class Legemiddel {

    // Variables
    public final String navn;
    private int pris;
    public final double mengdeVirkestoff;

    // Constructor
    public Legemiddel(String navn, int pris, double mengdeVirkestoff) {
        this.navn = navn;
        this.pris = pris;
        this.mengdeVirkestoff = mengdeVirkestoff;
    }

    // To String
    @Override
    public String toString() {
        return "Navn: " + this.navn + "\nPris: " + this.pris + "\nMengde Virkestoff: " + this.mengdeVirkestoff;
    }

    // Methods
    public int hentPris() { return this.pris; }
    public void settNyPris(int pris) { this.pris = pris; }
}
