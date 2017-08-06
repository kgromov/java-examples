package golovach.lecture14.lab02;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 23.07.2017.
 */
public class FileLab_CopyDir {
    static int files, folders = 0;

    private static void copy(File src, File dst) {
        if (src.isDirectory()) {
            if (!dst.exists()) {
                dst.mkdir();
                folders++;
            }
            for (File srcSubDir : src.listFiles()) {
                String subDirName = srcSubDir.getName();
                copy(srcSubDir, new File(dst, subDirName));
            }
        } else {
            try (BufferedReader in = new BufferedReader(new FileReader(src));
                 BufferedWriter out = new BufferedWriter(new FileWriter(dst))) {
                String line;
             /*   int count;
                char[] chars = new char[1024];*/
                while ((line = in.readLine()) != null) {
//                while ((count = in.read(chars)) != -1) {
                    out.write(line);
//                    out.write(chars, 0, count);
                }
                files++;
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) {
        long startCopy = System.currentTimeMillis();
        copy(new File("D:\\java\\workspace"), new File("D:\\TEMP"));
        System.out.println(String.format("Copy %d folders and %f files for %d seconds", folders, files,
//                (System.currentTimeMillis() - startCopy)));
                TimeUnit.SECONDS.convert(System.currentTimeMillis() - startCopy, TimeUnit.MILLISECONDS)));
    }
}
