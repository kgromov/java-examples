package future;

import com.javamex.classmexer.MemoryUtil;

import java.nio.file.Paths;
import java.util.Vector;

/**
 * Created by konstantin on 04.04.2021.
 */
public class Runner {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        // Completable future completely sucks here
        FilesReader filesReader = new CompletableFutureFilesReader();
        filesReader.readFilesContentInFolder(Paths.get("D:\\workspace\\benchmarks\\benchmarks\\output"));
        filesReader = new ParallelStreamFilesReader();
        filesReader.readFilesContentInFolder(Paths.get("D:\\workspace\\benchmarks\\benchmarks\\output"));
        System.out.println("######################################");
        // Completable future wins 2 times if numOfThreads 2 * numOfProcessors
        Waiter waiter = new ParallelStreamWaiter();
        waiter.process();
        waiter = new CompletableFutureWaiter();
        long usedBytes = MemoryUtil.memoryUsageOf(waiter);
        long objectInBytes = MemoryUtil.memoryUsageOf(new Object());
        long booleanInBytes = MemoryUtil.memoryUsageOf(true); // 16 bytes - the same as object
        // old empty collection Vector has overhead of 88 bytes (ArrayList = 40 bytes)
        long booleanWrapperInBytes = MemoryUtil.deepMemoryUsageOf(new Vector<String>());
        waiter.process();
    }
}
