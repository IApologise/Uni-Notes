import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class KlargjørData {

    // Constants
    static final int ANTALL_TRÅDER = 8;

    public static void main(String[] args) {

        // Creating new file (just in case) and getting parent folder
        String fileName = args[0];
        File metadata = new File(fileName);
        String parentFolder = metadata.getParent() + "/";

        // Creating infected and non-infected monitor groups
        Subsekvensregister infectedRegister = new Subsekvensregister();
        Monitor infectedMonitor = new Monitor(infectedRegister);

        Subsekvensregister notInfectedRegister = new Subsekvensregister();
        Monitor notInfectedMonitor = new Monitor(notInfectedRegister);

        // Creating a list of reading threads for the future
        ArrayList<Thread> readingThreads = new ArrayList<Thread>();

        // Reading metadata
        Scanner scanner = null;
        try {
            scanner = new Scanner(metadata);
        } catch (FileNotFoundException e) {
            System.out.println("Error - Metadata file not found.");
            System.exit(0);
        }

        // Separating data into 2 monitor groups
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();                // Next line
            String[] nextLineSplit = nextLine.split(",");  // Splitting data into file path and infection state

            // Reading line
            String readFilePath = parentFolder + nextLineSplit[0];        // Reading file name
            boolean isInfected = Boolean.parseBoolean(nextLineSplit[1]);  // Reading patient infection state

            // Creating a reading thread and assigning the monitor group to the thread then adding it to an array
            Thread readingThread;
            if (isInfected) {
                readingThread = new Thread(new Lesetråd(readFilePath, infectedMonitor));
            } else {
                readingThread = new Thread(new Lesetråd(readFilePath, notInfectedMonitor));
            }
            readingThreads.add(readingThread);
        }

        // We are done reading metadata
        scanner.close();

        // Reading files
        for (Thread readingThread : readingThreads) {
            readingThread.start();
        }

        // Waiting for threads to finish
        for (Thread readingThread : readingThreads) {
            try {
                readingThread.join();
            } catch (InterruptedException e) {
                System.out.println("Error - Thread was interrupted.");
                continue;
            }
        }

        // Now we create a list of merging threads
        Thread[] mergingThreads = new Thread[ANTALL_TRÅDER * 2];
        for (int i = 0; i < ANTALL_TRÅDER; i++) {
            Thread newInfectedThread = new Thread(new Flettetråd(infectedMonitor));
            Thread newNotInfectedThread = new Thread(new Flettetråd(notInfectedMonitor));

            // Adding newly created threads to the list
            mergingThreads[i] = newInfectedThread;
            mergingThreads[i + ANTALL_TRÅDER] = newNotInfectedThread;
        }

        // Running threads - Merging frequency tables
        for (Thread mergingThread : mergingThreads) {
            mergingThread.start();
        }

        // Waiting for merging threads to finish
        for (Thread mergingThread : mergingThreads) {
            try {
                mergingThread.join();
            } catch (InterruptedException e) {
                System.out.println("Error - Thread was interrupted.");
                continue;
            }
        }

        // Now getting the frequency tables
        Frekvenstabell infectedFrequencyTable = infectedMonitor.taUt();
        Frekvenstabell notInfectedFrequencyTable = notInfectedMonitor.taUt();

        // Rewriting the files specified in the task
        infectedFrequencyTable.skrivTilFil("smittet");
        notInfectedFrequencyTable.skrivTilFil("ikke_smittet");

        // I decided to output the files here
        // Don't know if I had to do it here or in the parent folder
        // If so, just add parentFolder to the filnavn inputs
    }
}
