public class HvitResept extends Resept {

    // Variables
    private final String farge;

    // Constructor
    public HvitResept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit) {
        super(legemiddel, utskrivendeLege, pasientId, reit);
        this.farge = "hvit";  // ... It's white *awkward silence*
    }

    // Methods
    @Override
    public String farge() { return this.farge; }
    @Override
    public int prisÅBetale() { return this.hentLegemiddel().hentPris(); }
}
