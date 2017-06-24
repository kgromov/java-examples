package golovach.lecture09.example;

/**
 * Created by konstantin on 24.06.2017.
 */
public class SingleElementBuffer {
    private Integer elem;

    public synchronized void put(Integer newElem) throws InterruptedException {
        while (elem != null) {
            this.wait();
        }
        this.elem = newElem;
        this.notifyAll();
    }

    public synchronized int get() throws InterruptedException {
        while (elem == null) {
            this.wait();
        }
        int result = elem;
        elem = null;
        this.notifyAll();
        return result;
    }
}
