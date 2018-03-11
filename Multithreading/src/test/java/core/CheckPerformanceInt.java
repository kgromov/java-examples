package core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by konstantin on 24.02.2018.
 */
public class CheckPerformanceInt <T extends Runnable & Serializable> {
    <T extends Runnable & Serializable> void execute(T t) {}

    public static void main(String[] args) {
        new CheckPerformanceInt().execute((Runnable & Serializable) (() -> {}));

        int length = 1_000_000;
        Integer [] array = new Integer[length];
        // fill in
        for(int i = 0; i< length; i++){
            array[i] = i;
        }
        List<Integer> list = Arrays.asList(array);
        Iterator<Integer> iterator = list.iterator();
        // test with indexes
        long start1 = System.nanoTime();
        for(int i = 0; i< length; i++){
            int value = list.get(i);
//            System.out.println(value);
        }
        long start2 = System.nanoTime();
        // test with forEach
        for(int str : list){
            int value =str;
//            System.out.println(value);
        }
       /* long start3 = System.nanoTime();
        // test with Iterator
        while (iterator.hasNext()){
            String value = iterator.next();
        }
        long start4 = System.nanoTime();
        // test forEach stream
        list.forEach(str -> {
            String value = str;
        });
        long start5 = System.nanoTime();
        // test IntStream
        IntStream.range(0, length).forEach(i -> {
            String value = list.get(i);
        });*/

        System.out.println(String.format("Time to loop with IntStream = %d ns", System.nanoTime() - start2));
//        System.out.println(String.format("Time to loop with stream = %d ns", start5 - start4));
        System.out.println(String.format("Time to loop with indexes = %d ns", start2 - start1));
       /* System.out.println(String.format("Time to loop with forEach = %d ns", start3 - start2));
        System.out.println(String.format("Time to loop with iterator = %d ns", start4 - start3));*/

    }
}
