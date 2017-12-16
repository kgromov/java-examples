package golovach.collections.lists;

import java.util.Iterator;

/**
 * Created by konstantin on 11.12.2017.
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
        return new LinkedListIterator();
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
        for (int i = 0; i < size(); i++) {
            for (Node<E> node = first; node != null; node = node.next) {
                if (o == null ? node.item == null : node.item.equals(o)) {
                    unlink(node);
                    return true;
                }
            }
        }
        return false;
    }

    public E remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    // *** *** *** OBJECT METHODS *** *** ***
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof SimpleList)) return false;
        Iterator<E> it1 = this.iterator();
        Iterator<?> it2 = ((SimpleList<?>) o).iterator();
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
        Node<E> temp = first;
        hash = hash * (temp.item != null ? temp.item.hashCode() : 1);
        while (temp.next != null) {
            temp = temp.next;
            hash = hash * (temp.item != null ? temp.item.hashCode() : 1);
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        Iterator<E> it = iterator();
       /* // 1st case
        while (it.hasNext()) {
            E o = it.next();
            builder.append(o != null ? o.toString() : "null").append(", ");
        }*/
        // 2nd case
        Node<E> temp = first;
        while (temp.next != null) {
            builder.append(temp.item != null ? temp.item.toString() : "null").append(", ");
            temp = temp.next;
        }
        builder.append(temp.item != null ? temp.item.toString() : "null");
        builder.append("]");
        return builder.toString();
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

    private final class LinkedListIterator implements Iterator<E> {
        Node<E> cursor = first;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            try {
                return cursor.item;
            } finally {
                cursor = cursor.next;
            }
        }
    }

    public static void main(String[] args) {
        String[] array = {"a", "b", "c"};
        SimpleLinkedList<String> list1 = new SimpleLinkedList<>();
        SimpleLinkedList<String> list2 = new SimpleLinkedList<>();
        SimpleArrayList<String> sList = new SimpleArrayList<>();
        for (String str : array) {
            list1.add(str);
            list2.add(str);
            sList.add(str);
        }
        boolean isEq = list1.equals(list2);
        isEq = list1.equals(sList);
        list1.add("d");
        list1.remove("a");
        sList.remove("a");
        isEq = list1.equals(list2);
        for (Iterator<String> it = list1.iterator(); it.hasNext(); ) {
            System.out.print(it.next() + " ");
        }
    }
}
