package golovach.lecture09.lab01;

/**
 * Created by konstantin on 24.06.2017.
 */
public class SingleElementBuffer_v1 {
    private int waitedProducers;
    private int waitedConsumers;
    private Integer elem;

    public synchronized void put(Integer newElem) throws InterruptedException {
        while (elem != null) {
            waitedProducers++;
            this.wait();
            waitedProducers--;
        }
        this.elem = newElem;
        if (waitedProducers > 0) {
            this.notify();
        }
    }

    public synchronized int get() throws InterruptedException {
        while (elem == null) {
            waitedConsumers++;
            this.wait();
            waitedConsumers--;
        }
        int result = elem;
        elem = null;
        if (waitedConsumers > 0) {
            this.notify();
        }
        return result;
    }
}
