package future;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 04.04.2021.
 */
public interface FilesReader {

    default void readFilesContentInFolder(Path folder) {
        long start = System.nanoTime();
        readFiles(Arrays.asList(folder.toFile().listFiles()));
        System.out.println(String.format("%s: Time elapsed = %d ms", this.getClass().getSimpleName(),
                TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS)));
    }

    default List<String> readFile(File file) {
        try {
            return Files.readAllLines(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    void readFiles(List<File> files);
}
