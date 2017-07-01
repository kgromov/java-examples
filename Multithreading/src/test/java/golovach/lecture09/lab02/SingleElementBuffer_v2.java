package golovach.lecture09.lab02;

import golovach.lecture09.example.SingleElementBuffer;

/**
 * Created by konstantin on 24.06.2017.
 */
/* Works correctly, theoretically is better tnan original version
 * cause notify weak up proper thread
 * Works cause of unconditional notify at the end of method
 * notify inside loop unreachable
 *
 */
public class SingleElementBuffer_v2 extends SingleElementBuffer {
    private Integer elem;

    public synchronized void put(Integer newElem) throws InterruptedException {
        while (elem != null) {
            System.out.println("Fall asleep " + Thread.currentThread().getName());
            this.wait();
            if (elem != null) {
                System.out.println("Weak up in loop put " + Thread.currentThread().getName());
                this.notify();
            }
        }
        System.out.println("Weak up put " + Thread.currentThread().getName());
        this.elem = newElem;
        this.notify();
    }

    public synchronized int get() throws InterruptedException {
        while (elem == null) {
            System.out.println("Fall asleep " + Thread.currentThread().getName());
            this.wait();
            if (elem == null) {
                System.out.println("Weak up in loop get " + Thread.currentThread().getName());
                this.notify();
            }
        }
        int result = elem;
        elem = null;
        System.out.println("Weak up get " + Thread.currentThread().getName());
        this.notify();
        return result;
    }
}
