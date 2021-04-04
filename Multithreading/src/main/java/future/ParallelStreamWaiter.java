package future;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.04.2021.
 */
public class ParallelStreamWaiter extends Waiter {
    @Override
    protected void process(List<DummyWaitTask> tasks) {
        List<Integer> collect = tasks.parallelStream().map(DummyWaitTask::doSomeWork).collect(Collectors.toList());
        System.out.println(collect);
    }
}
