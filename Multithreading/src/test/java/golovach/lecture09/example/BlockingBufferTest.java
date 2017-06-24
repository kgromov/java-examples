
package golovach.lecture09.example;

/**
 * Created by konstantin on 24.06.2017.
 */
public class BlockingBufferTest {
    public static void main(String[] args) {
        SingleElementBuffer buffer = new SingleElementBuffer();
        new Thread(new Producer(0, 1000, buffer)).start();
        new Thread(new Consumer(buffer)).start();

    }
}
