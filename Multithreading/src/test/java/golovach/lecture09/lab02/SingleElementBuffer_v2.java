package golovach.lecture09.lab02;

/**
 * Created by konstantin on 24.06.2017.
 */
public class SingleElementBuffer_v2 {
    private Integer elem;

    public synchronized void put(Integer newElem) throws InterruptedException {
        while (elem != null) {
            this.wait();
            if (elem != null) {
                this.notify();
            }
        }
        this.elem = newElem;
        this.notify();
    }

    public synchronized int get() throws InterruptedException {
        while (elem == null) {
            this.wait();
            if (elem == null) {
                this.notify();
            }
        }
        int result = elem;
        elem = null;
        this.notify();
        return result;
    }
}
