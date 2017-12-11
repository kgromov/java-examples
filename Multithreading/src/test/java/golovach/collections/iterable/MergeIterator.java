package golovach.collections.iterable;

import java.util.Iterator;

/**
 * Created by konstantin on 08.12.2017.
 */
public class MergeIterator implements Iterator<Integer> {
    private final int max;
    private int current;

    public MergeIterator(int left, int right) {
        this.max = right;
        this.current = left;
    }

    @Override
    public boolean hasNext() {
        return current < max;
    }

    @Override
    public Integer next() {
        return current++;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
