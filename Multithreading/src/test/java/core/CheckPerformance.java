package core;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by konstantin on 24.02.2018.
 */
public class CheckPerformance {

    public static void main(String[] args) {
        List<String> elems = new ArrayList<>(Arrays.asList(
                "a", "b"
        ));
        elems = elems.subList(0, 1);
        Stream<String> stream = elems.stream();
        elems.add("c");
        stream.forEach(System.out::println);


        int length = 1_000_000;
        String [] array = new String[length];
        // fill in
        for(int i = 0; i< length; i++){
            array[i] = String.valueOf(i);
        }
        List<String> list = Arrays.asList(array);
        Iterator<String> iterator = list.iterator();
        // test with indexes
        long start1 = System.nanoTime();
        for(int i = 0; i< length; i++){
            String value = list.get(i);
//            System.out.println(value);
        }
        long start2 = System.nanoTime();
        // test with forEach
        for(String str : list){
            String value =str;
//            System.out.println(value);
        }
        long start3 = System.nanoTime();
        // test with Iterator
        while (iterator.hasNext()){
            String value = iterator.next();
        }
        long start4 = System.nanoTime();
        // test with Iterator
        Spliterator<String> spliterator = list.spliterator();
        spliterator.trySplit().forEachRemaining(str ->
        {
            String value = str;
        });
        long start5 = System.nanoTime();
        // test forEach stream
        list.forEach(str -> {
            String value = str;
        });
        long start6 = System.nanoTime();
        // test IntStream
        IntStream.range(0, length).forEach(i -> {
            String value = list.get(i);
        });

        System.out.println(String.format("Time to loop with IntStream = %d ns", System.nanoTime() - start6));
        System.out.println(String.format("Time to loop with stream = %d ns", start6 - start5));
        System.out.println(String.format("Time to loop with splititerator#forEachRemaining = %d ns", start5 - start4));
        System.out.println(String.format("Time to loop with iterator = %d ns", start4 - start3));
        System.out.println(String.format("Time to loop with forEach = %d ns", start3 - start2));
        System.out.println(String.format("Time to loop with indexes = %d ns", start2 - start1));

        /* As the conclusion:
         * 1) Loop with indexes is the fastest one ~ 1.5 faster than Iterator (do not creste implicitly Iterable);
         * 2) Loop with 1 explicit Iterator ~ 1.01 - 1.04 faster than forEach with implicit Iterator
         * 3) Loop with forEach (implicit Iterator) ~ 1.2 faster tna IntStream
         * 4) IntStream is 5 times faster than forEach with stream()
         */
    }
}
