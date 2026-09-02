public class Person {

    // Attributes
    private final Bil3 bil; // Function to change cars is not implemented, poor guy ;(

    // Constructor
    public Person(Bil3 referanse){
        this.bil = referanse;
    }

    // Methods
    public void skriv_ut_bil(){
        System.out.println("Bil nummeret til mennesket: " + bil.hentnummer());
    }
}
