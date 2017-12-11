package golovach.collections.lists;

import java.util.Iterator;

import static java.lang.Math.max;

/**
 * Created by konstantin on 11.12.2017.
 */
/*
1) remove(Object value)
2) iterator()
3) toString()
4) equals(Object other)
5) hashCode()
 */
public class SimpleArrayList<E> implements SimpleList<E> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private E[] data;
    private int size = 0;

    public SimpleArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public SimpleArrayList(int initialCapacity) {
        this.data = (E[]) new Object[initialCapacity];
    }

    // *** *** *** ADD *** *** ***
    @Override
    public boolean add(E newElement) {
        ensureCapacity(size + 1);
        data[size] = newElement;
        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        rangeCheck(index);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    // *** *** *** READ *** *** ***
    @Override
    public E get(int index) {
        rangeCheck(index);
        return data[index];
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    // *** *** *** CHECK *** *** ***
    @Override
    public boolean contains(Object element) {
        if (element == null) { // look for null
            for (int k = 0; k < size; k++) {
                if (null == data[k]) {
                    return true;
                }
            }
        } else { // look for !null
            for (int k = 0; k < size; k++) {
                if (element.equals(data[k])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // *** *** *** MUTATE *** *** ***
    @Override
    public E set(int index, E newElement) {
        rangeCheck(index);
        E oldElement = data[index];
        data[index] = newElement;
        return oldElement;
    }

    // *** *** *** REMOVE *** *** ***
    @Override
    public boolean remove(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        E oldValue = data[index];
        int numMoved = size - index - 1;
        System.arraycopy(data, index + 1, data, index, numMoved);
        data[--size] = null;
        return oldValue;
    }

    // *** *** *** OBJECT METHODS *** *** ***
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    // ---------- internals ----------
    private void rangeCheck(int index) {
        if (index < 0 || size < index) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCapacity = max(minCapacity, data.length + (data.length >> 1));
            E[] newData = (E[]) new Object[newCapacity];
            System.arraycopy(data, 0, newData, 0, data.length);
            this.data = newData;
        }
    }
}
