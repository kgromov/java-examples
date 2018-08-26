package core;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by konstantin on 30.06.2018.
 */
public class RenameFiles {

    public static void main(String[] args) {
        String path1 = "C:\\Users\\konstantin\\Desktop\\Днепр-Запорожье\\День_1";
        String path2 = "C:\\Users\\konstantin\\Desktop\\Днепр-Запорожье\\День_2";

        Comparator<File> comparator = Comparator.comparingLong(File::lastModified);

        File dir1 = new File(path1);
        List<File> files = Arrays.asList(dir1.listFiles());
        files.sort(comparator);

        // rename files dir1
        int filesCount = files.size();
        for (int i = 1; i <= filesCount; i++) {
            File file = files.get(i - 1);
            String destName = file.getName().replaceAll("[\\d]+", String.format("%04d", i));
            file.renameTo(new File(dir1.getAbsolutePath() + File.separator + destName));
        }

        files = Arrays.asList(new File(path2).listFiles());
        files.sort(comparator);
        int filesCount2 = files.size();
        // rename files dir2
        for (int i = 1; i <= filesCount2; i++) {
            File file = files.get(i - 1);
            String destName = file.getName().replaceAll("[\\d]+", String.format("%04d", i + filesCount));
            file.renameTo(new File(dir1.getAbsolutePath() + File.separator + destName));
        }
    }
}
