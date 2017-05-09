package golovach.lab02;

import golovach.Node;

/**
 * Created by konstantin on 06.05.2017.
 */
public class DNode {
    public int value;
    public DNode prev;
    public DNode next;

    public DNode(int value, DNode prev, DNode next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
}
