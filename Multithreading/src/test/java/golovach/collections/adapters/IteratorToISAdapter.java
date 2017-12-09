package golovach.collections.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by konstantin on 08.12.2017.
 */
public class IteratorToISAdapter extends InputStream {
    private final Iterator<Integer> iterator;

    public IteratorToISAdapter(Iterator<Integer> iterator) {
        this.iterator = iterator;
    }

    @Override
    public int read() throws IOException {
        return (iterator.hasNext()) ? iterator.next() : -1;
    }
}
