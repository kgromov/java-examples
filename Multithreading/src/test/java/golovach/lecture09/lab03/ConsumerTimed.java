package golovach.lecture09.lab03;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

/**
 * Created by konstantin on 24.06.2017.
 */
public class ConsumerTimed implements Runnable {
    private final int timeout;
    private final SingleElementBufferTimed buffer;

    public ConsumerTimed(int timeout, SingleElementBufferTimed buffer) {
        this.timeout = timeout;
        this.buffer = buffer;
        ++SingleElementBufferTimed_v2.consumerCount;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(buffer.get(timeout) + " consumed");
            } catch (InterruptedException | TimeoutException e) {
                System.out.println(Thread.currentThread().getName() + " time out");
                return;
            }
        }
    }
}
