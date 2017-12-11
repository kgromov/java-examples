package golovach.collections.iterable;

import static golovach.collections.iterable.IteratorUtils.interval;
import static golovach.collections.iterable.IteratorUtils.merge;
import static golovach.collections.iterable.IteratorUtils.squareInterval;

/**
 * Created by konstantin on 08.12.2017.
 */
public class TestInterval {
    public static void main(String[] args) {
        for (int k : merge(interval(10, 12), interval(10, 12))) {
            System.out.print(" " + k);
        }
        System.out.println();

        for (int k : merge(interval(10, 10), interval(10, 12))) {
            System.out.print(" " + k);
        }
        System.out.println();

        for (int k : merge(interval(10, 12), interval(10, 10))) {
            System.out.print(" " + k);
        }
        System.out.println();

        for (int k : merge(interval(10, 10), interval(10, 10))) {
            System.out.print(" " + k);
        }
        System.out.println();

        for (int k : merge(interval(0, 10), interval(1000, 1002))) {
            System.out.print(" " + k);
        }
        System.out.println();

        for (int k : merge(interval(1000, 1002), interval(0, 10))) {
            System.out.print(" " + k);
        }
        System.out.println();

        for (int k : merge(interval(0, 10), interval(5, 15))) {
            System.out.print(" " + k);
        }
        System.out.println();
    }
}
