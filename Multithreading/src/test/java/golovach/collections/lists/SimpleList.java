package golovach.collections.lists;

import java.util.Iterator;

/**
 * Created by konstantin on 11.12.2017.
 */
 public interface SimpleList<T> {

    // *** *** *** ADD *** *** ***
     boolean add(T newElement);

     void add(int index, T element);

    // *** *** *** READ *** *** ***
     T get(int index);

     Iterator<T> iterator();

    // *** *** *** CHECK *** *** ***
     boolean contains(Object hasElement);

     int size();

     boolean isEmpty();

    // *** *** *** MUTATE *** *** ***
     T set(int index, T newElement);

    // *** *** *** REMOVE *** *** ***
     boolean remove(Object o);

     T remove(int index);
}
