package golovach;

/**
 * Created by konstantin on 06.05.2017.
 */
public class Node {
    public int value;
    public Node next;

    public Node(int value, Node next) {
        this.value = value;
        this.next = next;
    }

    public boolean hasNext(Node node) {
        return node.next != null;
    }

    public Node getHead() {
        Node node = this.next;
        while (node.next != null) {
            node = node.next;
            if (!node.hasNext(node)) return node;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.value + "->");
        Node next = this.next;
        while (next != null) {
            builder.append(next.value).append("->");
            next = next.next;
        }
        builder.append("null");
        return builder.toString();
    }
}
