import java.util.Scanner;

public class GameOfLife {

    public static void main(String[] args){

        // Scanner
        Scanner scanner = new Scanner(System.in);

        // Asking user for the grid size
        System.out.print("How many rows? \n >>> ");
        int antRader = scanner.nextInt();
        System.out.print("How many columns? \n >>> ");
        int antKolonner = scanner.nextInt();

        // Creating and drawing world
        Verden verden = new Verden(antRader, antKolonner);
        verden.tegn();

        // Preparing for loop
        System.out.print("Leave empty to continue. \n >>> ");
        String output = scanner.nextLine();  // Skips first time for an unknown reason (Didn't read Java Docs)
        output = scanner.nextLine();

        // Updating world (loop)
        while (output.isEmpty()){
            verden.oppdatering();
            System.out.print("Leave empty to continue. \n >>> ");
            output = scanner.nextLine();
        }
    }
}
