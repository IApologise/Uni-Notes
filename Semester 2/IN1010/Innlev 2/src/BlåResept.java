public class BlåResept extends Resept {

    // Variables
    private final String farge;

    // Constructor
    public BlåResept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit) {
        super(legemiddel, utskrivendeLege, pasientId, reit);
        this.farge = "blå";  // Do I have to explain?
    }

    // Methods
    @Override
    public String farge() { return this.farge; }
    @Override
    public int prisÅBetale() {
        return (int) ((float) this.hentLegemiddel().hentPris() / 4 + 0.5);  // Rounding
    }
}
