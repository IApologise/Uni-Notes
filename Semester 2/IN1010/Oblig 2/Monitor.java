import java.util.concurrent.locks.*;

public class Monitor {

    // Locks and Conditions
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    // Declarations
    private final Subsekvensregister SubsequenceRegister;

    public Monitor(Subsekvensregister subsequenceRegister) {
        this.SubsequenceRegister = subsequenceRegister;
    }

    public void settInn(Frekvenstabell f) {
        lock.lock();
        try {
            this.SubsequenceRegister.settInn(f);
            condition.signalAll();  // Signaling that stuff was added for taUt()
        } finally {
            lock.unlock();
        }
    }
    
    public Frekvenstabell taUt() {
        lock.lock();  // Occupying the register

        // Performing operations
        try {
            while (this.antall() < 1) {
                condition.await();  // Waiting until something gets there
            }
            return this.SubsequenceRegister.taUt();

        // In case it gets interrupted
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;

        // Unlocking now
        } finally {
            lock.unlock();
        }
    }

    public Frekvenstabell[] taUtTo() {
        lock.lock();
        try {
            while (this.antall() < 2) {
                condition.await();
            }

            Frekvenstabell[] result = new Frekvenstabell[2];
            result[0] = this.SubsequenceRegister.taUt();
            result[1] = this.SubsequenceRegister.taUt();
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;

        } finally {
            lock.unlock();
        }
    }

    public int antall() {
        lock.lock();
        try {
            return this.SubsequenceRegister.antall();
        } finally {
            lock.unlock();
        }
    }

    public static Frekvenstabell les(String filnavn) {
        return Subsekvensregister.les(filnavn);
    }
}
