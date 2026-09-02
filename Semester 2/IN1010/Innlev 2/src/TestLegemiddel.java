public class TestLegemiddel {

    // Methods for testing
    private static boolean testNarkotiskId(Narkotisk narkotikk, int forventetLegemiddelId) {
        return narkotikk.id == forventetLegemiddelId;
    }

    private static boolean testVanedannendelId(Vanedannende vanedannende, int forventetLegemiddelId) {
        return vanedannende.id == forventetLegemiddelId;
    }

    private static boolean testVanliglId(Vanlig vanlig, int forventetLegemiddelId) {
        return vanlig.id == forventetLegemiddelId;
    }

    // Tests
    public static void main(String[] args) {

        // Konstruerer nye eksempel objekter (også early testing)
        // Narkotikka
        Narkotisk heroin = new Narkotisk("Heroin", 400, 80, 10);
        Narkotisk morphine = new Narkotisk("Heroin", 300, 100, 8);
        Narkotisk fentanyl = new Narkotisk("fentanyl", 540, 135, 14);

        // Vanedannende
        Vanedannende diazepam = new Vanedannende("Diazepam", 120, 150, 5);
        Vanedannende oxycodone = new Vanedannende("Oxycodone", 100, 200, 9);
        Vanedannende xanax = new Vanedannende("Xanax", 160, 260, 8);

        // Vanlige
        Vanlig paracetamol = new Vanlig("Paracetamol", 50, 550);
        Vanlig ibuprofen = new Vanlig("Ibuprofen", 75, 400);
        Vanlig aspirin = new Vanlig("Aspirin", 65, 480);


        // Modifisering testing
        // Narkotikka
        heroin.settNyPris(450);  // Mere

        // Vanedannende
        xanax.settNyPris(150);  // Mindre

        // Vanlige
        ibuprofen.settNyPris(75);  // Ingen endring


        // Print info testing
        System.out.println("\nInfo Testing\n");

        // Narkotikka
        System.out.println("Narkotikka:\n");
        System.out.println(heroin + "\n");
        System.out.println(morphine + "\n");
        System.out.println(fentanyl + "\n");

        // Vanedannende
        System.out.println("Vanedannende:\n");
        System.out.println(diazepam + "\n");
        System.out.println(oxycodone + "\n");
        System.out.println(xanax + "\n");

        // Vanlige
        System.out.println("Vanlige:\n");
        System.out.println(paracetamol + "\n");
        System.out.println(ibuprofen + "\n");
        System.out.println(aspirin + "\n");


        // ID testing
        System.out.println("\n\nID Testing:");

        // Narkotikka
        System.out.println("\nNarkotikka:\n");
        System.out.println(testNarkotiskId(heroin, 1));
        System.out.println(testNarkotiskId(fentanyl, 3));
        System.out.println(testNarkotiskId(morphine, 2));

        // Vanedannende
        System.out.println("\nVanedannende:\n");
        System.out.println(testVanedannendelId(diazepam, 1));
        System.out.println(testVanedannendelId(oxycodone, 2));
        System.out.println(testVanedannendelId(xanax, 3));

        // Vanlige
        System.out.println("\nVanlige:\n");
        System.out.println(testVanliglId(ibuprofen, 2));
        System.out.println(testVanliglId(paracetamol, 1));
        System.out.println(testVanliglId(aspirin, 3));
    }
}
