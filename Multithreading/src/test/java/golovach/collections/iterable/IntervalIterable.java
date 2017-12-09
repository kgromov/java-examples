package golovach.collections.iterable;

import java.util.Iterator;

/**
 * Created by konstantin on 08.12.2017.
 */
public class IntervalIterable implements Iterable<Integer> {
    private final int left;
    private final int right;

    public IntervalIterable(int left, int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntervalIterator(left, right);
    }
}
