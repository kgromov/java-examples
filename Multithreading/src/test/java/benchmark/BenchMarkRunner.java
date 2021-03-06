package benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchMarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
//                .include(MapBenchMark.class.getSimpleName())
//                .include(ListBenchMark.class.getSimpleName())
//                .include(FilesCounterBenchMark.class.getSimpleName())
//                .include(EnumBenchMark.class.getSimpleName())
//                .include(PairIndexBenchMark.class.getSimpleName())
//                .include(SingularFindIndexBenchMark.class.getSimpleName())
//                .include(PluralFindIndexBenchMark.class.getSimpleName())
//                .include(IntBoxingBenchMark.class.getSimpleName())
                .include(IntArrayBoxingBenchMark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
