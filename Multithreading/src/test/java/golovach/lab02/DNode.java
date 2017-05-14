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

    public DNode(int value) {
        this.value = value;
    }

    public DNode getHead() {
        DNode node = this.next;
        while (node.next != null) {
            node = node.next;
            if (!node.hasNext(node)) return node;
        }
        return null;
    }

    public DNode getTail() {
        DNode node = this.prev;
        while (node.prev != null) {
            node = node.prev;
            if (!node.hasPrev(node)) return node;
        }
        return null;
    }

    public boolean hasNext(DNode node) {
        return node.next != null;
    }

    public boolean hasPrev(DNode node) {
        return node.prev != null;
    }
}
