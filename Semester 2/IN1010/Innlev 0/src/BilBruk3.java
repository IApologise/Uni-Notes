public class BilBruk3 {
    public static void main(String[] args){

        // Building a car for future generations
        Bil3 ford = new Bil3("A8N32K");

        // Giving birth to a son and gifting him a car
        Person marcus = new Person(ford);

        // Interrogating Marcus to acquire his license plate information (I forgot it)
        marcus.skriv_ut_bil();
    }
}
