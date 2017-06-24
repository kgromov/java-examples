package golovach.lecture09.example;

/**
 * Created by konstantin on 24.06.2017.
 */
public class Consumer implements Runnable {
    private final SingleElementBuffer buffer;

    public Consumer(SingleElementBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println(buffer.get() + " consumed");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName());
                return;
            }
        }
    }
}
