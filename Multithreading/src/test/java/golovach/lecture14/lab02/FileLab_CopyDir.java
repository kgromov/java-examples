package golovach.lecture14.lab02;

import java.io.File;

/**
 * Created by konstantin on 23.07.2017.
 */
public class FileLab_CopyDir {

    private static void copy(File src, File dst) {
        if (src.isDirectory()) {
            if (!dst.exists()) {
                dst.mkdir();
            }
            for (File srcSubDir : src.listFiles()) {
                String subDirName = srcSubDir.getName();
                copy(srcSubDir, new File(dst, subDirName));
            }
        }
    }

    public static void main(String[] args) {
        copy(new File("d:/tmp"), new File("d:/tmp2"));
    }
}
