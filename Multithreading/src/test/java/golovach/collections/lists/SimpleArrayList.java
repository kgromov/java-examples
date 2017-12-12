package golovach.collections.lists;

import java.util.Iterator;

import static java.lang.Math.max;

/**
 * Created by konstantin on 11.12.2017.
 */
/*
1) remove(Object value)
2) iterator()
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
        return new ArrayIterator();
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
        for (int i = 0; i < size; i++) {
            E e = data[i];
            if (e == null ? element == null : e.equals(element)) {
                int numMoved = size - i - 1;
                System.arraycopy(data, i + 1, data, i, numMoved);
                data[--size] = null;
                return true;
            }
        }
        return false;
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
        if (o == null || !(o instanceof SimpleList)) return false;
        Iterator<E> it1 = this.iterator();
        Iterator<?> it2 = ((SimpleArrayList<?>) o).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            E o1 = it1.next();
            Object o2 = it2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2))) return false;
        }
        return !(it1.hasNext() || it2.hasNext());
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (E element : data) {
            hash += hash * (element != null ? 31 * element.hashCode() : 1);
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        if (size > 0) {
            for (int i = 0; i < size - 1; i++) {
                E element = data[i];
                builder.append(element != null ? element.toString() : "null").append(", ");
            }
            builder.append(data[size - 1] != null ? data[size - 1].toString() : "null");
        }
        builder.append("]");
        return builder.toString();
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

    private final class ArrayIterator implements Iterator<E> {
        int currentIndex;

        @Override
        public boolean hasNext() {
            return currentIndex != size;
        }

        @Override
        public E next() {
            try {
                return data[currentIndex];
            } finally {
                ++currentIndex;
            }
        }
    }
}
