package golovach.lecture10;

/**
 * Created by konstantin on 15.07.2017.
 */
public class ThreadNode {
    public Thread thread;
    public ThreadNode next;

    public ThreadNode(Thread thread, ThreadNode nextNode) {
        this.thread = thread;
        this.next = nextNode;
    }

    public ThreadNode getTail() {
        ThreadNode tail = this;
        ThreadNode iterator = this;
        while (iterator != null) {
            iterator = iterator.next;
            tail = iterator != null ? iterator : tail;
        }
        return tail;
    }

    public void add(ThreadNode node) {
        getTail().next = node;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.thread.getName() + "->");
        ThreadNode next = this.next;
        while (next != null) {
            builder.append(next.thread.getName()).append("->");
            next = next.next;
        }
        builder.append("null");
        return builder.toString();
    }
}