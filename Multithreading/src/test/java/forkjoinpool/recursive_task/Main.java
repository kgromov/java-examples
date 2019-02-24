package forkjoinpool.recursive_task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 23.02.2019.
 */
public class Main {
    public static void main(String[] args) {
        DocumentMock mock = new DocumentMock();
        String[][] document = mock.generateDocument(100, 1000, "the");

        long start = System.nanoTime();
        DocumentTask task = new DocumentTask(document, 0, 100, "the");
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(task);     // 1
//        pool.invoke(task);    // 2
       /* task.fork();          // 3
        System.out.println(task.join());*/
        do {
            System.out.printf("Main: Thread Count:%d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal:%d\n", pool.getStealCount());
            System.out.printf("Main: Parallelism:%d\n", pool.getParallelism());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());
        pool.shutdown();
        /*try {
            pool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if (task.isCompletedNormally()) {
            System.out.println("Main: The process has completed normally.");
        }

        try {
            System.out.printf("Main: The word appears %d in the document", task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Time elapsed = %d", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));

    }
}
