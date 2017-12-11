package golovach.collections.lists;

import java.util.Iterator;

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
public class SimpleLinkedList<E> implements SimpleList<E> {
    private Node<E> first = null; // head
    private Node<E> last = null; // tail
    private int size = 0;

    // *** *** *** ADD *** *** ***
    public boolean add(E newElement) {
        final Node<E> tmp = last;
        final Node<E> newNode = new Node<>(tmp, newElement, null);
        last = newNode;
        if (tmp == null) {
            first = newNode;
        } else {
            tmp.next = newNode;
        }
        size++;
        return true;
    }

    public void add(int index, E element) {
        checkIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    // *** *** *** READ *** *** ***
    public E get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    // *** *** *** CHECK *** *** ***
    public boolean contains(Object hasElement) {
        return indexOf(hasElement) != -1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // *** *** *** MUTATE *** *** ***
    public E set(int index, E newElement) {
        checkIndex(index);
        Node<E> foundNode = node(index);
        E oldVal = foundNode.item;
        foundNode.item = newElement;
        return oldVal;
    }

    // *** *** *** REMOVE *** *** ***
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
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
    private void checkIndex(int index) {
        if (index < 0 || size < index) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private Node<E> node(int index) {
        if (index < size / 2) {
            Node<E> tmp = first;
            for (int i = 0; i < index; i++) {
                tmp = tmp.next;
            }
            return tmp;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
    }

    private int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    private E unlink(Node<E> x) { //todo:
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    private void linkLast(E e) { //todo
        final Node<E> tmp = last;
        final Node<E> newNode = new Node<>(tmp, e, null);
        last = newNode;
        if (tmp == null) {
            first = newNode;
        } else {
            tmp.next = newNode;
        }
        size++;
    }

    private void linkBefore(E e, Node<E> succ) { //todo
        // assert succ != null;
        final Node<E> prev = succ.prev;
        final Node<E> newNode = new Node<>(prev, e, succ);
        succ.prev = newNode;
        if (prev == null) {
            first = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
    }

    private static class Node<T> {
        private Node<T> prev;
        private T item;
        private Node<T> next;

        private Node(Node<T> prev, T item, Node<T> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }
}
