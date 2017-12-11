package golovach.collections.iterable;

import java.util.*;

/**
 * Created by konstantin on 08.12.2017.
 */
public class IteratorUtils {
    public static Iterable<Integer> interval(int left, int right) {
        return new IntervalIterable(left, right);
    }

    public static Iterable<Integer> squareInterval(int left, int right) {
        return (Iterable<Integer>) () -> new SquareIterator(left, right);
    }

    public static Iterable<Integer> merge(Iterable<Integer> iter0, Iterable<Integer> iter1) {
        List<Integer> list =  new ArrayList<>();
        for(int i : iter0){
            list.add(i);
        }
        for(int i : iter1){
            list.add(i);
        }
        Collections.sort(list);
        return list;
    }
}
