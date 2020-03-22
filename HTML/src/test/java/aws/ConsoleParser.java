package aws;

import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ConsoleParser {

    public static final String UPDATE_REGION_COLUMN = "UpdateRegionName";
    public static final String OUTPUT_FILE_NAME = "ACCUMULATED_ERRORS_";

    public ConsoleParser() {
    }


    //    aws s3 cp <s3_path> <local_path> --recursive --exclude "*" --include "LC_*json" | --include "FB_MUC_*json"
    // Akela - FB_DEU_G4_SA_SO_build_number_288121_console_log.zip: {logfile.log; error.log}  = --include "<Product>*<UR>*console_log.zip"
    // HDLC - LC_AUT_EAST_BUILD_NUMBER_288123_HD_LOGS.zip: {}           = --include "<Product>*<UR>*HD_LOGS.zip"
    public static void main(String[] args) throws IOException {

        String logsFolder = "C:\\HERE-CARDS\\my_dev_presubmits\\18060\\NAR\\logs";
//        String errorToFind = "ERROR -> TEST_QUERY_RESULTS";
        String errorToFind = "ERROR ->";
        String errorToFind2 = "Link is missing during setting restricted manoeuvre";
        File csvOutputFile = new File(logsFolder + "\\ ACCUMULATED_ERRORS_MANOEUVRE.csv");
        String header = "UpdateRegionName,Details";
        Path csvOutputFilePath = Paths.get(csvOutputFile.getAbsolutePath());
        long start = System.nanoTime();
        // 6750 ms - 33 records
//        original(logsFolder, header, csvOutputFilePath, errorToFind, errorToFind2);
        // 4366 ms - 33 records
//        originalRefactored(logsFolder, header, csvOutputFilePath, errorToFind, errorToFind2);
        // 3917 ms - 33 records
//        pathFindSequential(logsFolder, header, csvOutputFilePath, errorToFind, errorToFind2);
        // 1188 ms - 33 records
//        pathFindInParallel(logsFolder, header, csvOutputFilePath, errorToFind, errorToFind2);
        // 1651 ms - 33 records (parallel inside = 1194 ms); {threshold: 200 = 2.2s; 100 = 1.6s; 50 = 1.3s; 25 = 1.1s}
//        pathFindIBatch(logsFolder, header, csvOutputFilePath, errorToFind, errorToFind2);
        // 348 ms - 33 records
        pathFindForkJoinPool(logsFolder, header, csvOutputFilePath, errorToFind, errorToFind2);
        System.out.println(String.format("Time elapsed = %d ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
    }

    private static void originalRefactored(String logsFolder, String header, Path csvOutputFilePath, String errorToFind, String errorToFind2) throws IOException {
        long count = 0L;
        File[] files = (new File(logsFolder)).listFiles();
        label351:
        for (File file : files) {
            if (file.isFile() && file.getName().contains("console_log")) {
                System.out.println("Processing " + file.getName() + " file.");
                String uRName = file.getName().toUpperCase();
                uRName = uRName.substring(0, uRName.toUpperCase().indexOf("_BUILD_NUMBER_"));
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                     Scanner in = new Scanner(zipInputStream)) // replace Scanner to more adequate class
                {
                    ZipEntry zipEntry;
                    do {
                        do {
                            if ((zipEntry = zipInputStream.getNextEntry()) == null) {
                                continue label351;
                            }
                        } while (zipEntry.isDirectory());
                    } while (!zipEntry.getName().contains("error.log"));

                    while (in.hasNext()) {
                        String line = in.nextLine();
                        if (count == 0L) {
                            System.out.println(header);
                            Files.write(csvOutputFilePath, header.getBytes(Charset.defaultCharset()));
                        }

                        if (line.contains(errorToFind) && line.contains(errorToFind2)) {
                            String data = '\n' + uRName + "," + line;
                            System.out.println(data);
                            Files.write(csvOutputFilePath, data.getBytes(Charset.defaultCharset()), StandardOpenOption.APPEND);
                        }

                        ++count;
                        if (count % 10000L == 0L && count > 1L) {
                            System.out.println("Processed " + count + " rows.");
                        }
                    }
                }
            }
        }
    }

    private static void pathFindSequential(String logsFolder, String header, Path csvOutputFilePath, String errorToFind, String errorToFind2) throws IOException {
        AtomicLong count = new AtomicLong();
        Files.find(Paths.get(logsFolder),
                Short.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile()
                        && filePath.getFileName().toString().contains("console_log"))
                .map(Path::toFile)
                .forEach(file ->
                {
                    System.out.println("Processing " + file.getName() + " file.");
                    String uRName = file.getName().toUpperCase();
                    uRName = uRName.substring(0, uRName.toUpperCase().indexOf("_BUILD_NUMBER_"));
                    try (FileInputStream fileInputStream = new FileInputStream(file);
                         ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                         Scanner in = new Scanner(zipInputStream))  // replace Scanner to more adequate class
                    {
                        ZipEntry zipEntry;
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                            if (zipEntry.getName().contains("error.log")) {
                                break;
                            }
                        }

                        while (in.hasNext()) {
                            String line = in.nextLine();
                            if (count.get() == 0L) {
                                System.out.println(header);
                                Files.write(csvOutputFilePath, header.getBytes(Charset.defaultCharset()));
                            }

                            if (line.contains(errorToFind) && line.contains(errorToFind2)) {
                                String data = '\n' + uRName + "," + line;
                                System.out.println(data);
                                Files.write(csvOutputFilePath, data.getBytes(Charset.defaultCharset()), StandardOpenOption.APPEND);
                            }
                            count.incrementAndGet();
                            if (count.get() > 1L && count.get() % 10000L == 0L) {
                                System.out.println("Processed " + count + " rows.");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void pathFindInParallel(String logsFolder, String header, Path csvOutputFilePath, String errorToFind, String errorToFind2) throws IOException {
        AtomicLong count = new AtomicLong();
        List<String> lines = new ArrayList<>();
        Files.find(Paths.get(logsFolder),
                Short.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.getFileName().toString().contains("console_log"))
                .map(Path::toFile)
                .sorted()
                .parallel()
                .forEach(file ->
                {
                    System.out.println("Processing " + file.getName() + " file.");
                    String uRName = file.getName().toUpperCase();
                    uRName = uRName.substring(0, uRName.toUpperCase().indexOf("_BUILD_NUMBER_"));
                    try (FileInputStream fileInputStream = new FileInputStream(file);
                         ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                         Scanner in = new Scanner(zipInputStream))  // replace Scanner to more adequate class
                    {
                        ZipEntry zipEntry;
                        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                            if (zipEntry.getName().contains("error.log")) {
                                break;
                            }
                        }

                        while (in.hasNext()) {
                            String line = in.nextLine();
                            if (line.contains(errorToFind) && line.contains(errorToFind2)) {
                                String data = uRName + "," + line;
                                System.out.println(data);
                                synchronized (lines) {
                                    lines.add(data);
                                }
                            }
                            count.incrementAndGet();
                            if (count.get() > 1L && count.get() % 10000L == 0L) {
                                System.out.println("Processed " + count + " rows.");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        if (!lines.isEmpty()) {
            lines.add(0, header);
            Files.write(csvOutputFilePath, lines);
        }
    }

    private static void pathFindIBatch(String logsFolder, String header, Path csvOutputFilePath, String errorToFind, String errorToFind2) throws IOException {
        List<File> files = Files.find(Paths.get(logsFolder),
                Short.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.getFileName().toString().contains("console_log"))
                .map(Path::toFile)
                .collect(Collectors.toList());
        int threshold = 25;
        int portions = files.size() / threshold;
        int leftOver = files.size() % threshold;
        if (leftOver > 0 && leftOver > threshold >> 1) {
            ++portions;
        }
        List<ZipStripe> stripes = new ArrayList<>(portions);
        Thread[] threads = new Thread[portions];
        for (int i = 0; i < portions; i++) {
            ZipStripe stripe = new ZipStripe();
            stripes.add(stripe);
            int startIndex = threshold * i;
            int endIndex = (i == portions - 1) ? files.size() : threshold * (i + 1) - 1;
            threads[i] = new Thread(() -> stripe.process(files.subList(startIndex, endIndex), errorToFind, errorToFind2));
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        List<String> lines = stripes.stream()
                .map(ZipStripe::getLines)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if (!lines.isEmpty()) {
            lines.add(0, header);
            Files.write(csvOutputFilePath, lines);
        }
    }

    private static void pathFindForkJoinPool(String logsFolder, String header, Path csvOutputFilePath, String errorToFind, String errorToFind2) throws IOException {
        List<File> files = Files.find(Paths.get(logsFolder),
                Short.MAX_VALUE,
                (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.getFileName().toString().contains("console_log"))
                .map(Path::toFile)
                .collect(Collectors.toList());
        ZipStripeTask task = new ZipStripeTask(files, 0, files.size(), errorToFind, errorToFind2, new ArrayList<>());
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(task);

        do {
            ;
           /* System.out.printf("Main: Thread Count:%d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal:%d\n", pool.getStealCount());
            System.out.printf("Main: Parallelism:%d\n", pool.getParallelism());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        } while (!task.isDone());

        pool.shutdown();

        List<String> lines = task.getResult();
        if (!lines.isEmpty()) {
            lines.add(0, header);
            Files.write(csvOutputFilePath, lines);
        }
    }

    // or callable probably; try 2 methods design
    @Getter
    private static final class ZipStripe {
        private final List<String> lines;

        public ZipStripe() {
            this.lines = new ArrayList<>();
        }

        public void process(List<File> filesRange, String errorToFind, String errorToFind2) {
//            AtomicLong count = new AtomicLong();
            filesRange.stream()
                      /*.sorted()
                      .parallel()*/
                    .forEach(file ->
                    {
                        System.out.println("Processing " + file.getName() + " file.");
                        String uRName = file.getName().toUpperCase();
                        uRName = uRName.substring(0, uRName.toUpperCase().indexOf("_BUILD_NUMBER_"));
                        try (FileInputStream fileInputStream = new FileInputStream(file);
                             ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                             Scanner in = new Scanner(zipInputStream))  // replace Scanner to more adequate class
                        {
                            ZipEntry zipEntry;
                            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                                if (zipEntry.getName().contains("error.log")) {
                                    break;
                                }
                            }

                            long count = 0L;
                            while (in.hasNext()) {
                                String line = in.nextLine();
                                if (line.contains(errorToFind) && line.contains(errorToFind2)) {
                                    String data = uRName + "," + line;
                                    System.out.println(data);
                                    synchronized (lines) {
                                        lines.add(data);
                                    }
                                }
//                                count.incrementAndGet();
                                ++count;
//                                if (count.get() > 1 && count.get() % 10000L == 0) {
                                if (count > 1 && count % 10000L == 0) {
                                    System.out.println("Processed " + count + " rows.");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private static final class ZipStripeTask extends RecursiveAction
    {
        private static final int THRESHOLD = 25;

        private final List<File> filesRange;
        private final int startIndex;
        private final int endIndex;
        private final String errorToFind;
        private final String errorToFind2;
        @Getter
        private final List<String> result;

        public ZipStripeTask(List<File> filesRange, int startIndex, int endIndex, String errorToFind, String errorToFind2, List<String> result) {
            this.filesRange = filesRange;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.errorToFind = errorToFind;
            this.errorToFind2 = errorToFind2;
            this.result = result;
        }

        @Override
        protected void compute() {
            if (endIndex - startIndex < THRESHOLD) {
                ZipStripe stripe = new ZipStripe();
                stripe.process(filesRange, errorToFind, errorToFind2);
                result.addAll(stripe.getLines());
            } else {
                int middle = (startIndex + endIndex) / 2;
                System.out.printf("ZipStripeTask: Pending tasks:%s\n", getQueuedTaskCount());
                ZipStripeTask t1 = new ZipStripeTask(filesRange.subList(startIndex, middle + 1), startIndex, middle + 1, errorToFind, errorToFind2, result);
                ZipStripeTask t2 = new ZipStripeTask(filesRange.subList(middle + 1, endIndex), middle + 1, endIndex, errorToFind, errorToFind2, result);
                invokeAll(t1, t2);
                result.addAll(t2.getResult());
                result.addAll(t1.getResult());
            }
        }
    }
}
