package golovach.lecture09.lab03;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

/**
 * Created by konstantin on 24.06.2017.
 */
public class ProducerTimed implements Runnable {
    private int startValue;
    private final  int period;
    private final int timeout;
    private final SingleElementBufferTimed buffer;

    public ProducerTimed(int startValue, int period, int timeout, SingleElementBufferTimed buffer) {
        this.startValue = startValue;
        this.period = period;
        this.timeout = timeout;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println(startValue + " produced");
                buffer.put(startValue++, timeout);
                Thread.sleep(period);
            } catch (InterruptedException | TimeoutException e) {
                System.out.println(Thread.currentThread().getName());
                return;
            }
        }
    }
}
