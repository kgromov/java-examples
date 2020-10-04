package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 04.10.2020.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 2)
@Measurement(iterations = 3)
public class AddElementToTheEndBenchmark {
    @Param({"10"})
    private int N;

    private List<String> data;

//    private List<String> arrayList;
    private List<String> linkedList;
//    private Set<String> hashSet;
//    private Set<String> treeSet;
    private Set<String> linkedHashSet;

    @Setup
    public void setup() {
//        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
//        hashSet = new HashSet<>();
//        treeSet = new TreeSet<>();
        linkedHashSet = new LinkedHashSet<>();
        data = getData(N);
    }

 /*   @Benchmark
    public void addToArrayList(Blackhole blackhole) {
        for (String value : data) {
            blackhole.consume(arrayList.add(value));
        }
    }*/

    @Benchmark
    public void addToArrayLinkedList(Blackhole blackhole) {
        for (String value : data) {
            blackhole.consume(linkedList.add(value));
        }
    }

 /*   @Benchmark
    public void addToArrayHashSet(Blackhole blackhole) {
        for (String value : data) {
            blackhole.consume(hashSet.add(value));
        }
    }*/

/*    @Benchmark
    public void addToArrayTreeSet(Blackhole blackhole) {
        for (String value : data) {
            blackhole.consume(treeSet.add(value));
        }
    }*/

    @Benchmark
    public void addToArrayLinkedHashSet(Blackhole blackhole) {
        for (String value : data) {
            blackhole.consume(linkedHashSet.add(value));
        }
    }

    private static List<String> getData(int size) {
        List<String> data = new ArrayList<>(size);
//        ThreadLocalRandom random = ThreadLocalRandom.current();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int nextInt = random.nextInt(size);
            data.add(String.valueOf(nextInt));
        }
        System.out.println(String.format("N = %d, unique values = %d", size, new HashSet<>(data).size()));
        return data;
    }
}
