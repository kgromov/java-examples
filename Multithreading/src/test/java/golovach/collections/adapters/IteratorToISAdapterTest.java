package golovach.collections.adapters;

import java.io.IOException;
import java.io.InputStream;

import static golovach.collections.iterable.IteratorUtils.interval;

/**
 * Created by konstantin on 08.12.2017.
 */
public class IteratorToISAdapterTest {
    public static void main(String[] args) throws IOException {
        InputStream is = new IteratorToISAdapter(interval(0, 10).iterator());
        int k;
        while ((k = is.read()) != -1) {
            System.out.print(" " + k);
        }
    }
}
