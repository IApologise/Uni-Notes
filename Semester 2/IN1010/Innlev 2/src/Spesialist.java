public class Spesialist extends Lege implements Godkjenningsfritak {

    // Variables
    private final String kontrollkode;

    // Constructor
    public Spesialist(String navn, String kontrollkode) {
        super(navn);
        this.kontrollkode = kontrollkode;  // Not used though
    }

    // To string
    @Override
    public String toString() {
        return super.toString() + "\nKontroll Kode: " + this.hentKontrollkode();
    }

    // Methods
    @Override
    public String hentKontrollkode() { return this.kontrollkode; }
}
