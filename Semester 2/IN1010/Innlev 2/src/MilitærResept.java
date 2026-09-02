public class MilitærResept extends HvitResept {

    // Constructor
    public MilitærResept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId) {
        super(legemiddel, utskrivendeLege, pasientId, 3);  // Always 3 uses
    }

    // Methods
    @Override
    public int prisÅBetale() {
        return 0;  // Free
    }
}
