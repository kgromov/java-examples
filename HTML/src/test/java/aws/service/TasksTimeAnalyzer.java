package aws.service;

import aws.model.Downloader;
import aws.model.LogsHolder;
import aws.model.Settings;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TasksTimeAnalyzer {
    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        long start = System.nanoTime();
        try {
            // init settings
            Settings settings = new Settings.Builder()
                    .product(Sets.newHashSet("LC", "FB", "3D"))
//                    .updateRegion("DEU_G8")//
                    .map1Path("s3://akela-artifacts/Akela-19135/CMP-1f6e524/logs")
                    /*.map2Path("s3://akela-artifacts/Akela-19135/CMP-0c3fe8b/logs")
                    .map1Path("s3://akela-artifacts/Akela-19135/CMP-1430c6d/logs/")*/
                    .map2Path("s3://akela-artifacts/Akela-19135/CMP-a5811da/logs/")
                    .outputDir("C:\\HERE-CARDS\\my_dev_presubmits\\trash\\demo")
                    .build();

            // download logs ?

            // create tasks holder per each product

            String outputDir = settings.getLogsFolder().toString();
            settings.getProducts().stream().parallel().forEach(product ->
            {
                try {
                    List<Future<?>> results = new ArrayList<>(2);
                    results.add(executor.submit(() -> {
                        try {
                            new Downloader(product, settings.getUpdateRegion(), settings.getMap1Path(), outputDir).download(System.out::println);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }));
                    results.add(executor.submit(() -> {
                        try {
                            new Downloader(product, settings.getUpdateRegion(), settings.getMap2Path(), outputDir).download(System.out::println);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }));
                    results.forEach(r -> {
                        try {
                            r.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });

                    LogsHolder logsHolder = new LogsHolder(product);

                    logsHolder.bypassLogs(settings);
                } catch (Exception e) {

                }
            });
            // clear logs folder -> move to method
            Files.walkFileTree(settings.getLogsFolder(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } finally {
            executor.shutdown();
            System.out.println(String.format("Time elapsed = %d s", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));

        }
    }
}
