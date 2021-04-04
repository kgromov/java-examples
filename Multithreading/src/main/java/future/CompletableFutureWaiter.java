package future;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.04.2021.
 */
public class CompletableFutureWaiter extends Waiter {
    @Override
    protected void process(List<DummyWaitTask> tasks) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(tasks.size(), processors)*2);
        List<CompletableFuture<Integer>> futures = tasks.stream()
                .map(t -> CompletableFuture.supplyAsync(t::doSomeWork, executorService))
                .collect(Collectors.toList());
        List<Integer> collect = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        System.out.println(collect);

        // transformations
      /*  List<CompletableFuture<Void>> collect1 = tasks.stream()
                .map(t -> CompletableFuture.supplyAsync(t::doSomeWork, executorService)
                        .thenApply(Math::abs)
                        .thenApplyAsync(Math::sqrt)
                        .thenAccept(System.out::println)
                ).collect(Collectors.toList());
        collect1.forEach(CompletableFuture::join);*/
    }
}
