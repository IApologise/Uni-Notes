public class Vanlig extends Legemiddel {

    // Variables
    private static int gid = 0;  // Previously used identifier
    public final int id;

    // Constructor
    public Vanlig(String navn, int pris, double mengdeVirkestoff) {
        super(navn, pris, mengdeVirkestoff);
        gid++;  // Possible integer overflow resulting in multiple objects with same id. Sad ;(

        this.id = gid;
    }
}
