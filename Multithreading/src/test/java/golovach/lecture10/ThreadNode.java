package golovach.lecture10;

/**
 * Created by konstantin on 15.07.2017.
 */
public class ThreadNode {
    public final Thread thread;
    public final ThreadNode nextNode;

    public ThreadNode(Thread thread, ThreadNode nextNode) {
        this.thread = thread;
        this.nextNode = nextNode;
    }
}
