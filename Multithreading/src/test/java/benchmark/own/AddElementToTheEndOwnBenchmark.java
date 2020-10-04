package benchmark.own;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 04.10.2020.
 */
public class AddElementToTheEndOwnBenchmark {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddElementToTheEndOwnBenchmark.class);
    private static final int[] SIZES = {10, 100, 1000};
    private static final InitCollectionData COLLECTION_DATA = InitCollectionData.getInstance(SIZES);

    public static void main(String[] args) {
        for (int N : SIZES) {
            LOGGER.info("Benchmark for N = {}", N);
            List<String> arrayList = new ArrayList<>();
            List<String> linkedList = new LinkedList<>();
            Set<String> hashSet = new HashSet<>();
            Set<String> treeSet = new TreeSet<>();
            Set<String> linkedHashSet = new LinkedHashSet<>();

            testAddToEndWithContains(arrayList, N);
            testAddToEndWithContains(linkedList, N);
            testAddToEnd(hashSet, N);
            testAddToEnd(treeSet, N);
            testAddToEnd(linkedHashSet, N);
        }
    }

    private static void testAddToEnd(Collection<String> collection, int n) {
        List<String> data = COLLECTION_DATA.getData().get(n);
        long start = System.nanoTime();
        for (String value : data) {
            collection.add(value);
        }
        long end = System.nanoTime() - start;
        long total = TimeUnit.MICROSECONDS.convert(end, TimeUnit.NANOSECONDS);
        long perIteration = TimeUnit.MICROSECONDS.convert(end / n, TimeUnit.NANOSECONDS);
        LOGGER.info("N = {}, collectionType = {}, total time = {} ms, avg = {} ms",
                n, collection.getClass().getSimpleName(), total, perIteration);
    }

    private static void testAddToEndWithContains(Collection<String> collection, int n) {
        List<String> data = COLLECTION_DATA.getData().get(n);
        long start = System.nanoTime();
        for (String value : data) {
            if (!collection.contains(value)) {
                collection.add(value);
            }
        }
        long end = System.nanoTime() - start;
        long total = TimeUnit.MICROSECONDS.convert(end, TimeUnit.NANOSECONDS);
        long perIteration = TimeUnit.MICROSECONDS.convert(end / n, TimeUnit.NANOSECONDS);
        LOGGER.info("N = {}, collectionType = {}, total time = {} ms, avg = {} ms",
                n, collection.getClass().getSimpleName(), total, perIteration);
    }
}
