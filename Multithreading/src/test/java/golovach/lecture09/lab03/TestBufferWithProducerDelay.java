package golovach.lecture09.lab03;

/**
 * Created by konstantin on 24.06.2017.
 */
public class TestBufferWithProducerDelay {
    public static void main(String[] args) {
        SingleElementBufferTimed bufferTimed = new SingleElementBufferTimed_v2();
        new Thread(new ConsumerTimed(1000, bufferTimed), "Consumer").start();

        new Thread(new ProducerTimed(1, 1200, 100, bufferTimed), "Producer").start();
        new Thread(new ProducerTimed(100, 1200, 100, bufferTimed), "Producer").start();
        new Thread(new ProducerTimed(10000, 1200, 100, bufferTimed), "Producer").start();

    }
}
