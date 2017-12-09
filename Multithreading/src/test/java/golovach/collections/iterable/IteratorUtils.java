package golovach.collections.iterable;

/**
 * Created by konstantin on 08.12.2017.
 */
public class IteratorUtils {
    public static Iterable<Integer> interval(int left, int right) {
        return new IntervalIterable(left, right);
    }
}
