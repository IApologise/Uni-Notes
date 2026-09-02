import java.io.File;
import java.util.Scanner;

public class ReadFile {
    public static void main(String[] args) {
        Scanner s = null;
        try {
            s = new Scanner(new File ("data.txt"));
        } catch (Exception e) {
            System.out.println("Kan ikke lese data.txt");
            System.exit(1);
        }
        System.out.println(s.nextLine());
    }
}
