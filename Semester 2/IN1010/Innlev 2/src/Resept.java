public abstract class Resept {

    // Variables
    private final Legemiddel legemiddel;
    private final Lege utskrivendeLege;
    private final int pasientId;
    private int reit;

    // Constructor
    public Resept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit) {
        this.legemiddel = legemiddel;
        this.utskrivendeLege = utskrivendeLege;
        this.pasientId = pasientId;
        this.reit = reit;
    }

    // To string
    @Override
    public String toString() {
        return "Legemiddel:\n" + this.legemiddel.toString() + "\nLege:\n" + this.hentLege().toString() + "\nPasient ID: " + this.hentId() + "\nReit: " + this.hentReit() + "\nPris: " + this.prisÅBetale() + "\nFarge: " + this.farge();
    }

    // Methods
    public int hentId() { return this.hentPasientId(); }
    public Legemiddel hentLegemiddel() { return this.legemiddel; }
    public Lege hentLege() { return this.utskrivendeLege; }
    public int hentPasientId() { return this.pasientId; }
    public int hentReit() { return this.reit; }
    public boolean bruk() {
        return this.reit-- > 0;  // Possible integer overflow, but look at how simple it is
    }

    // Abstract methods
    public abstract String farge();
    public abstract int prisÅBetale();
}
