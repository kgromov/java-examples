package forkjoinpool.recursive_action.own;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by konstantin on 24.03.2019.
 */
public class Main {
    private static int fileCounter(File directory, AtomicInteger filesCount)
    {
        File[] files = directory.listFiles();
        if (files == null) {
            return 0;
        }
        for (File file : files) {
            if (file.isFile()) {
                filesCount.incrementAndGet();
            } else {
                fileCounter(file, filesCount);
            }
        }
        return filesCount.get();
    }

    // TODO: with Files#walk - java.nio

    public static void main(String[] args) {
        long start = System.nanoTime();

        File rootDirectory = new File("D:\\");
//        System.out.println(fileCounter(rootDirectory, new AtomicInteger()));
        //
        FileCounterTask task = new FileCounterTask(rootDirectory);
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(task);

        do {
            System.out.printf("Main: Thread Count:%d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal:%d\n", pool.getStealCount());
            System.out.printf("Main: Parallelism:%d\n", pool.getParallelism());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        pool.shutdown();
        if (task.isCompletedNormally()) {
            System.out.println("Main: The process has completed normally.");
        }
        try {
            System.out.printf("Main: Total files in %s = %d\n", rootDirectory.getAbsolutePath(), task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("Time elapsed = %d", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));

    }
}
