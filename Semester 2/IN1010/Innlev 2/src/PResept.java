public class PResept extends HvitResept {

    // Constructor
    public PResept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit) {
        super(legemiddel, utskrivendeLege, pasientId, reit);  // This code is super. Haha, get it? Cuz it's... nevermind
    }

    // Methods
    @Override
    public int prisÅBetale() {
        int prisÅBetale = super.prisÅBetale();
        return prisÅBetale > 108 ? prisÅBetale - 108 : 0;  // 108 kron discount
    }
}