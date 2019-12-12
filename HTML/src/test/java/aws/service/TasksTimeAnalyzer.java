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
import java.util.concurrent.TimeUnit;

public class TasksTimeAnalyzer
{
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        try {
            // init settings
            Settings settings = new Settings.Builder()
                    .product(Sets.newHashSet("LC", "FB"))
                    .updateRegion("DEU_G8")
                    .map1Path("s3://akela-artifacts/Akela-191G0/CMP-3d406b4/logs/")
                    .map2Path("s3://akela-artifacts/Akela-191G0/CMP-c54ca67/logs/")
                    .outputDir("C:\\HERE-CARDS\\my_dev_presubmits\\trash\\demo")
                    .build();

            // download logs ?

            // create tasks holder per each product

            String outputDir = settings.getLogsFolder().toString();
            settings.getProducts().forEach(product ->
            {
                try {
                    // TODO: parallel
                    new Downloader(product, settings.getUpdateRegion(), settings.getMap1Path(), outputDir).download(System.out::println);
                    new Downloader(product, settings.getUpdateRegion(), settings.getMap2Path(), outputDir).download(System.out::println);

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
        }
        finally {
            System.out.println(String.format("Time elapsed = %d ms", TimeUnit.SECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));

        }


    }
}
