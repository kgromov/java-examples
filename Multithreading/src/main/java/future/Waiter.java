package future;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by konstantin on 04.04.2021.
 */
public abstract class Waiter {

    public void process() {
        List<DummyWaitTask> tasks = IntStream.range(0, 10).mapToObj(i -> new DummyWaitTask()).collect(Collectors.toList());
        long start = System.nanoTime();
        process(tasks);
        System.out.println(String.format("%s: Time elapsed = %d ms", this.getClass().getSimpleName(),
                TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
    }

    protected abstract void process(List<DummyWaitTask> tasks);
}
