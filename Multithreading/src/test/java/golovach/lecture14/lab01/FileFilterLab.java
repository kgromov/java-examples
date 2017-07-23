package golovach.lecture14.lab01;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by konstantin on 23.07.2017.
 */
public class FileFilterLab {
    static List<String> extensions = Arrays.asList(".jpg", ".png", ".bmp", ".xml");
    static FileFilter filter = file -> {
        String fileName = file.getName();
        return file.isFile() && file.length() > 1024 * 3
                && fileName.endsWith(".jpg") | fileName.endsWith(".png") |
                fileName.endsWith(".bmp") | fileName.endsWith(".xml");
    };

    public static File print(File root) {
        if (filter.accept(root)) return root;
        else {
            try {
                for (File file : root.listFiles()) {
                    print(file);
                }
            } catch (NullPointerException ignored) {
            }
        }
        return null;
    }

    public static File[] concatenate(File[] file0, File[] file1) {
        File[] result = new File[file0.length + file1.length];
        System.arraycopy(file0, 0, result, 0, file0.length);
        System.arraycopy(file1, 0, result, file0.length, file1.length);
        return result;
    }

    public static void main(String[] args) {
        File file = new File("D:\\workspace\\konstantin-examples");
       print(file);
    }
}
