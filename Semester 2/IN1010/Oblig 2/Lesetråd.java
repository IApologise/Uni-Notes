public class Lesetråd implements Runnable {

    // Declarations
    private final String filePath;
    private final Monitor monitor;

    // Constructor
    public Lesetråd(String filnavn, Monitor monitor) {
        this.filePath = filnavn;
        this.monitor = monitor;
    }

    public void run() {
        Frekvenstabell frequencyTable = Monitor.les(this.filePath);
        this.monitor.settInn(frequencyTable);
    }
}
