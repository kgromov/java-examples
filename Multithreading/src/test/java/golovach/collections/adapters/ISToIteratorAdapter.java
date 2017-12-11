package golovach.collections.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by konstantin on 08.12.2017.
 */
public class ISToIteratorAdapter implements Iterator<Byte> {
    private final InputStream is;
    private byte _byte;

    public ISToIteratorAdapter(InputStream is) {
        this.is = is;
    }

    @Override
    public boolean hasNext() {
        try {
            _byte = (byte) is.read();
        } catch (IOException e) {
            return false;
        }
        return _byte != -1;
    }

    @Override
    public Byte next() {
        return _byte;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

