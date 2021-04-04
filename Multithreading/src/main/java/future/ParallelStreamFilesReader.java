package future;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.04.2021.
 */
public class ParallelStreamFilesReader implements FilesReader {
    @Override
    public void readFiles(List<File> files) {
        List<String> totalLines = files.parallelStream()
                .map(this::readFile)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(String.format("Processed %d files, total lines = %d", files.size(), totalLines.size()));
    }
}
