public class Lege {

    // Variables
    private final String navn;

    // Constructor
    public Lege(String navn) {
        this.navn = navn;
    }

    // To string
    @Override
    public String toString() {
        return "Navn: " + this.hentNavn();
    }

    // Methods
    public String hentNavn() { return navn; }
}
