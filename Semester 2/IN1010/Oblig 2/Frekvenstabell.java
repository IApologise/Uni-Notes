import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class Frekvenstabell extends TreeMap<String, Integer> {

    @Override
    public String toString() {

        // String builder for optimization
        StringBuilder stringBuilder = new StringBuilder();

        // Adding stuff to the string builder
        for (String key : this.keySet()) {
            stringBuilder.append(key);            // Key
            stringBuilder.append(" ");            // Separator
            stringBuilder.append(this.get(key));  // Value
            stringBuilder.append("\n");           // Next line
        }

        // Converting to string and returning result
        return stringBuilder.toString();
    }

    public static Frekvenstabell flett(Frekvenstabell f1, Frekvenstabell f2) {

        // Creating a new treemap
        Frekvenstabell merged = new Frekvenstabell();
        merged.putAll(f1);  // Copying f1

        // Adding counts of f2
        for (String key : f2.keySet()) {
            Integer count1 = 0;
            Integer count2 = f2.get(key);

            // Checking if count exists
            if (f1.containsKey(key)) {
                count1 = f1.get(key);
            }

            Integer count = count1 + count2;  // Total count
            merged.put(key, count);           // Updating counts
        }

        // Returning merged treemap
        return merged;
    }

    public void skrivTilFil(String filnavn) {
        try {
            FileWriter fileWriter = new FileWriter(filnavn);
            fileWriter.write(this.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error, file not found.");
        }
    }
}