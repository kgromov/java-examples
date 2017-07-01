package golovach.lecture09.lab01;

import golovach.lecture09.example.SingleElementBuffer;

/**
 * Created by konstantin on 24.06.2017.
 */
/* No conditions to notify waited consumer:
 *  when waitedProducers == 0 consumer is already sleep
 *  when waitedProducers > 0 - producer fall asleep, but consumer is already sleep
 *  So, deadlock happened
*/
public class SingleElementBuffer_v1 extends SingleElementBuffer {
    private int waitedProducers;
    private int waitedConsumers;
    private Integer elem;

    public synchronized void put(Integer newElem) throws InterruptedException {
        while (elem != null) {
            waitedProducers++;
            System.out.println("Fall asleep " + Thread.currentThread().getName());
            this.wait();
            System.out.println("waitedProducers after wait() = " + waitedProducers);
            waitedProducers--;
        }
        this.elem = newElem;
        if (waitedProducers > 0) {
            System.out.println("waitedProducers = " + waitedProducers);
            System.out.println("Weak up" + Thread.currentThread().getName());
            this.notify();
        }
    }

    public synchronized int get() throws InterruptedException {
        while (elem == null) {
            waitedConsumers++;
            System.out.println("Fall asleep " + Thread.currentThread().getName());
            this.wait();
            System.out.println("waitedConsumers after wait() = " + waitedConsumers);
            waitedConsumers--;
        }
        int result = elem;
        elem = null;
        if (waitedConsumers > 0) {
            System.out.println("waitedConsumers = " + waitedConsumers);
            System.out.println("Weak up" + Thread.currentThread().getName());
            this.notify();
        }
        return result;
    }
}
