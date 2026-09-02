import java.util.concurrent.locks.*;

public class Main {
    public static void main(String[] args) {

        class Monitor {
            private int val;
            private ReentrantLock lock = new ReentrantLock();
            private Condition condition = lock.newCondition();

            public Monitor(int val) {
                this.val = val;
            }

            public int get() {
                return val;
            }
            public void add(int val) {
                this.val = Math.max(this.val, val);
            }
            public void sub(int val) {
                this.val -= val;
            }
        }

        class Ok implements Runnable {
            private Monitor monitor;

            public Ok(Monitor monitor) {
                this.monitor = monitor;
            }

            @Override
            public void run() {
                monitor.add(5);
                monitor.sub(3);
                System.out.println(monitor.get());
            }
        }

        Thread newThread = new Thread(new Ok(new Monitor(-2)));
        newThread.start();
        try {
            newThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}