package future;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by konstantin on 04.04.2021.
 */
public class DummyWaitTask {

    public int doSomeWork() {
//        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return ThreadLocalRandom.current().nextInt();
        }
    }
}
