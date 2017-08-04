package golovach.lecture09.lab03;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

/**
 * Created by konstantin on 24.06.2017.
 */
public class SingleElementBufferTimed_v2 extends SingleElementBufferTimed{
    private Integer elem;
    public static int producerCount = -1;
    public static int consumerCount = -1;

    public synchronized void put(Integer newElem, long timeout) throws InterruptedException, TimeoutException {
        Thread.sleep(SingleElementBufferTimed_v2.producerCount * 400);
        long waitTime = timeout;
        while (elem != null && waitTime > 0) {
            long start = System.currentTimeMillis();
            this.wait(waitTime);
            long end = System.currentTimeMillis();
            waitTime -= (end - start);
        }
        if (elem != null) {
//            System.out.println("Consumer did not get elem after timeout = " + timeout);
            throw new TimeoutException();
        }
        this.elem = newElem;
        this.notifyAll();
    }

    public synchronized int get(long timeout) throws InterruptedException, TimeoutException {
        Thread.sleep(SingleElementBufferTimed_v2.consumerCount * 400);
        long waitTime = timeout;
        while (elem == null && waitTime > 0) {
            long start = System.currentTimeMillis();
            this.wait(waitTime);
            long end = System.currentTimeMillis();
            waitTime -= (end - start);
        }
        if (elem == null) {
//            System.out.println("Producer did not put elem after timeout = " + timeout);
            throw new TimeoutException();
        }
        int result = elem;
        elem = null;
        this.notifyAll();
        return result;
    }
}
