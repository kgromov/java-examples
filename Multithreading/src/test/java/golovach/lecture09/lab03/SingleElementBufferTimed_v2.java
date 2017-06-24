package golovach.lecture09.lab03;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

/**
 * Created by konstantin on 24.06.2017.
 */
public class SingleElementBufferTimed_v2 {
    private Integer elem;

    public synchronized void put(Integer newElem, long timeout) throws InterruptedException, TimeoutException {
        long waitTime = timeout;
        while (elem != null && waitTime > 0) {
            long start = System.currentTimeMillis();
            this.wait(waitTime);
            long end = System.currentTimeMillis();
            waitTime -= (end - start);
        }
        this.elem = newElem;
        this.notifyAll();
    }

    public synchronized int get(long timeout) throws InterruptedException, TimeoutException {
        long waitTime = timeout;
        while (elem == null && waitTime > 0) {
            long start = System.currentTimeMillis();
            this.wait(waitTime);
            long end = System.currentTimeMillis();
            waitTime -= (end - start);
        }
        int result = elem;
        elem = null;
        this.notifyAll();
        return result;
    }
}
