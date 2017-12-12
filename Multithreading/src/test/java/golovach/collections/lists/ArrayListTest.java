package golovach.collections.lists;

import java.util.Iterator;

/**
 * Created by konstantin on 12.12.2017.
 */
public class ArrayListTest {
    public static void main(String[] args) {
        String[] array = {"a", "b", "c", "d"};
        SimpleList<String> list = new SimpleArrayList<>();
        SimpleList<String> list2 = new SimpleArrayList<>();
        for (String letter : array) {
            list.add(letter);
            list2.add(letter);
        }
        boolean result = list.equals(list2);
        result = list.remove("d");
        result = list.remove("d");
        String str = list.remove(0);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }

    }
}
