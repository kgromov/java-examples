package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 04.10.2020.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class CollectionsBenchmark {

    @State(Scope.Thread)
    public static class MyState {
        private Collection<String> employeeSet = new HashSet<>();
        private Collection<String> employeeList = new ArrayList<>();
        private Random random = new Random();
        private int iterations = 100000;
        private String employee = String.valueOf(random.nextInt(iterations));

        @Setup(Level.Trial)
        public void setUp() {


            for (long i = 0; i < iterations; i++) {
                String value = String.valueOf(random.nextInt(iterations));
                employeeSet.add(value);
                employeeList.add(value);
            }
            employeeSet.add(employee);
            employeeList.add(employee);
            System.out.println("Setup inside state");
        }
    }

    @Benchmark
    public boolean testArrayList(MyState state) {
        return state.employeeList.add(state.employee);
    }

    @Benchmark
    public boolean testHashSet(MyState state) {
        return state.employeeSet.add(state.employee);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(CollectionsBenchmark.class.getSimpleName())
                .threads(1)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .jvmArgs("-server")
                .build();
        new Runner(options).run();
    }
}
