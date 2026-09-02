import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Subsekvensregister {
    private final ArrayList<Frekvenstabell> register = new ArrayList<>();
    private static final int SUBSEQUENCELENGTH = 3;

    public void settInn(Frekvenstabell f) {
        register.add(f);
    }

    public Frekvenstabell taUt() {
        Frekvenstabell removed = register.getFirst();
        register.removeFirst();
        return removed;
    }

    public int antall() {
        return register.size();
    }

    public static Frekvenstabell les(String filnavn) {

        // Opening a file
        Scanner scanner;
        try {
            File file = new File(filnavn);
            scanner = new Scanner(file);
        }

        // File not found error handling
        catch (FileNotFoundException e) {
            System.out.println("Error - File not found.");
            return null;
        }

        // Saving values to the frequency table
        Frekvenstabell frequencyTable = new Frekvenstabell();
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();  // Next line

            // Getting all the combinations as specified in the task
            int combinations = nextLine.length() - SUBSEQUENCELENGTH + 1;
            for (int i = 0; i < combinations; i++) {

                // Adding combination to the frequency table
                String combination = nextLine.substring(i, i + SUBSEQUENCELENGTH);
                frequencyTable.put(combination, 1);
            }
        }

        // Closing a file and returning the frequency table
        scanner.close();
        return frequencyTable;
    }
}
