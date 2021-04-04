package future;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.04.2021.
 */
public class CompletableFutureFilesReader implements FilesReader {

    @Override
    public void readFiles(List<File> files) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(files.size(), processors));
        List<CompletableFuture<List<String>>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> readFile(file), executorService))
                .collect(Collectors.toList());
        List<String> totalLines = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(String.format("Processed %d files, total lines = %d", files.size(), totalLines.size()));
    }
}
