package golovach.lecture10;

/**
<<<<<<< HEAD
 * Created by kgr on 7/12/2017.
=======
 * Created by konstantin on 15.07.2017.
>>>>>>> 62f75c01d482fe2e67f092765305feac5a571269
 */
public class InterruptBufferTest {
    public static void main(String[] args) {
        InterruptBuffer buffer = new InterruptBuffer();
        Thread[] producers = new Thread[]{
                new Thread(new InterruptedProducer(1, buffer), "Producer_1"),
                new Thread(new InterruptedProducer(100, buffer), "Producer_100"),
                new Thread(new InterruptedProducer(1000, buffer), "Producer_1000"),
        };
        for (Thread producer : producers) {
            producer.start();
        }
        Thread[] consumers = new Thread[]{
                new Thread(new InterruptedConsumer(buffer), "Consumer_1"),
                new Thread(new InterruptedConsumer(buffer), "Consumer_2"),
        };
        for (Thread consumer : consumers) {
            consumer.start();
        }
    }
}
