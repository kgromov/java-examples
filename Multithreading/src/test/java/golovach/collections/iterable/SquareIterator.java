package golovach.collections.iterable;

import java.util.Iterator;

/**
 * Created by konstantin on 08.12.2017.
 */
public class SquareIterator implements Iterator<Integer> {
    private final int max;
    private int current;

    public SquareIterator(int left, int right) {
        this.max = right;
        this.current = left;
    }

    @Override
    public boolean hasNext() {
        return current * current < max;
    }

    @Override
    public Integer next() {
        try {
            return current * current;
        } finally {
            current++;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
