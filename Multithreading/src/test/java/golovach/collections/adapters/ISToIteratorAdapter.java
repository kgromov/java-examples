package golovach.collections.adapters;

import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by konstantin on 08.12.2017.
 */
public class ISToIteratorAdapter implements Iterator<Integer> {
    private final InputStream is;

    public ISToIteratorAdapter(InputStream is) {
        this.is = is;

    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer next() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

