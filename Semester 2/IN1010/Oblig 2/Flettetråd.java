public class Flettetråd implements Runnable {

    // Declarations
    private final Monitor monitor;

    // Constructor
    public Flettetråd(Monitor monitor) {
        this.monitor = monitor;
    }

    public void run() {
        while (monitor.antall() > 1) {
            Frekvenstabell[] frequencyTables = this.monitor.taUtTo();
            Frekvenstabell mergedFrequencyTable = Frekvenstabell.flett(frequencyTables[0], frequencyTables[1]);
            this.monitor.settInn(mergedFrequencyTable);
        }
    }
}
