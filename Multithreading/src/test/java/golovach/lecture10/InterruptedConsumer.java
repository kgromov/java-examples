package golovach.lecture10;

/**
 * Created by kgr on 7/12/2017.
 */
public class InterruptedConsumer implements Runnable {
    private final InterruptBuffer buffer;

    public InterruptedConsumer(InterruptBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int elem = buffer.get();
            System.out.println(elem + " consumed");
        }
    }
}
