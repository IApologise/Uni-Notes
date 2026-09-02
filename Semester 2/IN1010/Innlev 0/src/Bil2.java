public class Bil2 extends Bil1 {

    // Attributes
    protected final String nummer; // Car can only have 1 license plate number, no takesies-backsies

    // Constructor
    public Bil2(String bilnummer){
        super();

        this.nummer = bilnummer;
    }

    // Methods
    @Override
    public void skriv_ut(){
        System.out.println("Bil nummeret er: " + nummer);
    }
}
