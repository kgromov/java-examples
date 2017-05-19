package golovach.lecture04.lab01;

import golovach.lecture04.Node;

import java.util.Arrays;

/**
 * Created by konstantin on 06.05.2017.
 */
public class NodeUtils {

    public static Node createQueue(int size) {
        Node node = null;
        for (int i = 0; i <= size; i++) {
            node = new Node(i, node);
        }
        return node;
    }

    public static Node createQueue2(int size) {
        return size > 0
                ? new Node(size, createQueue2(size - 1))
                : new Node(0, null);
    }

    // lab 1
    public static int size(Node node) {
        int size = 1;
        while (node.next != null) {
            ++size;
            node = node.next;
        }
        return size;
    }

    public static int size2(Node node) {
        return node.next != null ? size2(node.next) + 1 : 1;
    }

    public static int sum(Node node) {
        int sum = 0;
        while (node.next != null) {
            sum += node.value;
            node = node.next;
        }
        return sum;
    }

    public static int sum2(Node node) {
        return node.next != null
                ? node.value + sum2(node.next)
                : node.value;
    }

    public static int max(Node node) {
        int max = node.value;
        while (node.next != null) {
            node = node.next;
            max = node.value - max > 0 ? node.value : max;
        }
        return max;
    }

    public static int max2(Node node) {
       return Math.max((node.next != null) ? max(node.next) : node.value, node.value);
    }

    // lab 2
    public static void add(Node list, int value) {
        Node head = list.getHead();
        head.next = new Node(value, null);
    }

    public static void add(Node list, int value, int index) {
        if (index == 0) {
            list = new Node(value, list);
            return;
        }
        if (index == 1) {
            list.next = new Node(value, list.next);
            return;
        }
        --index;
        Node next = list.next;
        for (int i = 1; next != null; i++) {
            if (i == index) {
                next.next = new Node(value, next.next);
                break;
            }
            next = next.next;
        }
    }

    public static void remove(Node node) {
        Node next = node.next;
        while (next.next != null) {
            next = next.next;
            if (!next.hasNext(next.next))
                next.next = null;
        }
    }

    public static void remove(Node node, int index) {
        int count = 0;
        Node current = node;
        if (index == 0) {
            current = current.next;
            return;
        }
        while (current.next != null) {
            Node next = current.next;
            if (count == index - 1) {
                // delete current node
                current.next = next.next != null ? next.next : null;
                break;
            }
            current = next;
            count++;
        }
    }

    public static void main(String[] args) {
        Node list = createQueue(5);
        Node list2 = createQueue2(5);
        System.out.println(list.toString());
        System.out.println(list2.toString());
        // ------- Lab 01 -------
        // bode size
        System.out.println("Size of list iterative = " + size(list));
        System.out.println("Size of list recursive = " + size2(list));
//        System.out.println(size2(list));
        // sum
        System.out.println("Sum all nodes values iterative = " + sum(list));
        System.out.println("Sum all nodes values recursive = " + sum2(list));
        // max
        System.out.println("Max value in list iterative = " + max(list));
        System.out.println("Max value in list recursive = " + max2(list));
        // ------- Lab 02 -------
        add(list, 12);
        System.out.println("Add to the head\t" + list.toString());
        Arrays.asList(1, 2, 0).forEach(index -> {
            add(list, index * 100, index);
            System.out.println(String.format("Add to index '%d'\t%s", index, list.toString()));
        });
        remove(list);
        System.out.println("Remove from the head\t" + list.toString());
        Arrays.asList(1, 2, 0).forEach(index -> {
            remove(list, index);
            System.out.println(String.format("Remove from the head at index '%d'\t%s", index, list.toString()));
        });
    }
}
