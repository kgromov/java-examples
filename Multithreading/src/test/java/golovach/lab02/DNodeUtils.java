package golovach.lab02;

/**
 * Created by konstantin on 13.05.2017.
 */
public class DNodeUtils {

    public static DNode createLinkedList(int size) {
        DNode head = null;
        DNode tail = null;
        for (int i = 0; i <= size; i++) {
            tail = new DNode(i, null, tail);
        }
        for (int i = size; i >= 0; i--) {
            tail.prev = head;
            head = tail;
            tail = tail.next;
        }
        return head;
    }

    public static int size(DNode node) {
        int size = 1;
        while (node.next != null) {
            ++size;
            node = node.next;
        }
        return size;
    }

    public static void add(DNode list, int value) {
        while (list.next != null) {
            list = list.next;
        }
        list.next = new DNode(value, list, null);
    }

    public static void add(DNode list, int value, int index) {
        if (index == size(list) - 1) {
            add(list, value);
        } else if (index == 0) {
            DNode tail = list.getTail();
            tail.prev = new DNode(value, null, tail);
        } else {
            --index;
            DNode node = list.getTail();
            for (int i = 1; node.next != null; i++) {
                if (i == index) {
                    DNode newNode = new DNode(value, node, node.next);
                    node.next.prev = newNode;
                    node.next = newNode;
                    break;
                }
                node = node.next;
            }
        }
    }

    public static void remove(DNode dNode) {
        dNode = dNode.getTail();
        dNode.next = null;
    }

    public static void remove(DNode list, int index) {
        if (index == size(list) - 1) {
            remove(list);
        } else if (index == 0) {
            DNode tail = list.getTail();
            tail.next.prev = null;
        } else {
            DNode node = list.getTail();
            for (int i = 1; node.next != null; i++) {
                if (i == index) {
                    DNode prev = node.prev;
                    node.prev.next = node.next;
                    node.next.prev = prev;
                    break;
                }
                node = node.next;
            }
        }
    }

    public static void printFromTail(DNode dNode) {
        StringBuilder builder = new StringBuilder(dNode.value + "->");
        DNode next = dNode.next;
        while (next != null) {
            builder.append(next.value).append("->");
            next = next.next;
        }
        builder.append("null");
        System.out.println(builder.toString());
    }

    public static void printFromHead(DNode dNode) {
        StringBuilder builder = new StringBuilder(dNode.value + "<-");
        DNode prev = dNode.prev;
        while (prev != null) {
            builder.append(prev.value).append("<-");
            prev = prev.prev;
        }
        builder.append("null");
        System.out.println(builder.toString());
    }

    public static void main(String[] args) {
        DNode tail = new DNode(0);
        DNode head = new DNode(1);
        DNode node = new DNode(11, tail, head);
        tail.next = node;
        head.prev = node;
        printFromTail(tail);
        printFromHead(head);
        DNode queue = createLinkedList(5);
        printFromHead(queue);
        add(queue, 21);
        printFromHead(queue);
        add(queue, 100, 0);
        printFromHead(queue);
        add(queue, 100, 5);
        printFromHead(queue);
        remove(queue);
        printFromHead(queue);
        remove(queue, 1);
        printFromHead(queue);
        remove(queue, 3);
        printFromHead(queue);
        remove(queue, 0);
        printFromHead(queue);
    }
}
