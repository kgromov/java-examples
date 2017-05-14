package golovach.lab04;

import golovach.lab03.TNode;

/**
 * Created by konstantin on 09.05.2017.
 */
public class FibonacciTree {

    public static TNode createFibonacciTree(int index) {
        return index > 1 ? new TNode(fibonacci(index), createFibonacciTree(index - 1), createFibonacciTree(index - 1)) : new TNode(1, null, null);
    }

    public static int fibonacci(int index) {
        return index == 1 | index == 2 ? 1 : fibonacci(index - 1) + fibonacci(index - 2);
    }

    public static void main(String[] args) {
        TNode tree = createFibonacciTree(5);
    }
}
