package golovach.lecture10;

/**
 * Created by konstantin on 15.07.2017.
 */
public class InterruptBuffer {
    private ThreadNode producers = null;
    private ThreadNode consumers = null;
    private Integer elem = null;

    public synchronized void put(int newElem) {
        while (elem != null) {
            try {
                // add new thread to the tail
                if (producers == null) {
                    this.producers = new ThreadNode(Thread.currentThread(), null);
                } else {
                    producers.add(new ThreadNode(Thread.currentThread(), null));
                }
                System.out.println("Add producer to queue:\t" + producers.toString());
                this.wait();
            } catch (InterruptedException e) {/*NOP*/}
        }
        elem = newElem;
        if (consumers != null) {
            System.out.println("Consumers queue before interrupt:\t" + consumers.toString());
            consumers.thread.interrupt();
            // remove from the tail
            consumers = consumers.next != null ? consumers.next : null;
            System.out.println("Consumers queue after interrupt:\t" + consumers);
        }
    }

    public synchronized int get() {
        while (elem == null) {
            try {
                // add new thread to the tail
                if (consumers == null) {
                    this.consumers = new ThreadNode(Thread.currentThread(), null);
                } else {
                    consumers.add(new ThreadNode(Thread.currentThread(), null));
                }
                System.out.println("Add consumer to queue:\t" + consumers.toString());
                this.wait();
            } catch (InterruptedException e) {/*NOP*/}
        }
        int result = elem;
        elem = null;
        if (producers != null) {
            System.out.println("Producers queue before interrupt:\t" + producers.toString());
            producers.thread.interrupt();
            // remove from the tail
            producers = producers.next != null ? producers.next : null;
            System.out.println("Producers queue after interrupt:\t" + producers);
        }
        return result;
    }
}