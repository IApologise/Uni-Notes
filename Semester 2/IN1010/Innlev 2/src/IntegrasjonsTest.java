public class IntegrasjonsTest {

    // Just gonna copy-paste from TestResepter.
    public static void main(String[] args) {

        // Konstruerer nye eksempel legemiddler (copy paste fra TestLegemiddel
        Narkotisk fentanyl = new Narkotisk("fentanyl", 540, 135, 14);
        Vanedannende xanax = new Vanedannende("Xanax", 160, 260, 8);
        Vanlig ibuprofen = new Vanlig("Ibuprofen", 75, 400);


        // Konstruerer nye leger
        Lege megIFremtiden = new Lege("Justas");
        Spesialist patrickStar = new Spesialist("Patrick Star", "5318008");  // BOOBIES


        // Konstruerer nye eksempel resepter
        HvitResept ffffffResept = new HvitResept(xanax, patrickStar, 255255255, 1);  // Flashbang
        MilitærResept militærResept = new MilitærResept(ibuprofen, megIFremtiden, 789);  // Why was 7 afraid of 9?
        PResept peeRecipe = new PResept(fentanyl, patrickStar, 8, 1);  // Get it? *Yet another awkward silence*
        BlåResept resept = new BlåResept(ibuprofen, megIFremtiden, 1, 222);  // Because 2 2s


        // Print og funksjon testing
        // Hvit Resept
        System.out.println("\nHvit Resept");
        ffffffResept.bruk();
        System.out.println(ffffffResept);

        // Militær Resept
        System.out.println("\nMilitær Resept");
        militærResept.bruk();
        System.out.println(militærResept);

        // P-Resept
        System.out.println("\nP-Resept");
        peeRecipe.bruk();
        System.out.println(peeRecipe);

        // Blå Resept
        System.out.println("\nBlå Resept:");
        resept.bruk();
        System.out.println(resept);
    }
}
