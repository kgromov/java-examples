package golovach.collections.adapters;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

/**
 * Created by konstantin on 08.12.2017.
 */
public class ISToIteratorAdapterTest {
    public static void main(String[] args) {
        Iterator<Byte> iter3
//                = new ISToIteratorAdapter(new ByteArrayInputStream("Hello World!".getBytes()));
//                = new ISToIteratorAdapter(new ByteArrayInputStream(new String(new char[]{'b', 'a', 'c'}).getBytes()));
                =  new ISToIteratorAdapter(new ByteArrayInputStream(new byte[]{10, 20, 30}));
        while (iter3.hasNext()) {
//            System.out.print(" " + (char)iter3.next().byteValue());
            System.out.print(" " + iter3.next());
        }
        System.out.println();

        Iterator iter0
                = new ISToIteratorAdapter(new ByteArrayInputStream(new byte[0]));
        while (iter0.hasNext()) {
            System.out.print(" " + iter0.next());
        }
        System.out.println();

        Iterator iter1
                = new ISToIteratorAdapter(new ByteArrayInputStream(new byte[]{10}));
        while (iter1.hasNext()) {
            System.out.print(" " + iter1.next());
        }
        System.out.println();
    }
}
