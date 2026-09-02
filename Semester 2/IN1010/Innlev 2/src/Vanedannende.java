public class Vanedannende extends Legemiddel {

    // Variables
    private static int gid = 0;  // Previously used identifier
    public final int id;
    public final int styrke;

    // Constructor
    public Vanedannende(String navn, int pris, double mengdeVirkestoff, int styrke) {
        super(navn, pris, mengdeVirkestoff);
        gid++;  // Also sad ;(

        this.styrke = styrke;
        this.id = gid;
    }

    // To string
    @Override
    public String toString() {
        return super.toString() + "\nStyrke: " + this.styrke;
    }
}
