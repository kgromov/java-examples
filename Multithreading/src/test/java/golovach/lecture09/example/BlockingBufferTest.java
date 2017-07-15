
package golovach.lecture09.example;

import golovach.lecture09.lab02.SingleElementBuffer_v2;

/**
 * Created by konstantin on 24.06.2017.
 */
public class BlockingBufferTest {
    public static void main(String[] args) {
        SingleElementBuffer buffer = new SingleElementBuffer_v2();
        Thread thread1 = new Thread(new Producer(0, 1000, buffer));
        thread1.setName("Producer");
        thread1.start();
        Thread thread2 = new Thread(new Consumer(buffer));
        thread2.setName("Consumer");
        thread2.start();

    }
}
