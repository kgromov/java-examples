import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by konstantin on 05.03.2017.
 */
public class ReadFile {
    private static String fileContent = "";
    private static volatile boolean lock = false;

    public static void readFile(String filePath) {
        try {
            File file = new File(filePath);
            InputStream is = new FileInputStream(file);
            FileReader reader = new FileReader(filePath);
            LineNumberReader lineNumberReader = new LineNumberReader(new BufferedReader(reader));
            lineNumberReader.lines().forEach(line -> fileContent += line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String filePath) {
        try {
        /*    while (lock) {
                System.out.println("Thread " + Thread.currentThread().getName() + " is waiting ...");
                if (lock) Thread.sleep(10);
            }*/
//            synchronized (fileContent) {
                lock = true;
                System.out.println("Thread " + Thread.currentThread().getName() + " is appending ...");
                StringBuffer buffer = new StringBuffer(Thread.currentThread().getName());
                buffer.append(System.lineSeparator());
                Files.write(Paths.get(filePath), buffer.toString().getBytes(), StandardOpenOption.APPEND);
//            }
//            lock = false;
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }


    public static void appendFile(FileWriter writer) {
        try {
            while (lock) {
                System.out.println("Thread " + Thread.currentThread().getName() + " is waiting ...");
                if (lock) Thread.sleep(10);

            }
            synchronized (writer) {
                lock = true;
//                BufferedWriter bw = new BufferedWriter(writer);
//                System.out.println("Thread " + Thread.currentThread().getName() + " is appending ...");
                writer.append(Thread.currentThread().getName());
           /*     bw.newLine();
                bw.flush();
                bw.close();*/
//                writer.close();
            }
            lock = false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        String filePath = "D:\\routerPasswords.txt";
        File file = new File(filePath);
        final FileWriter writer = new FileWriter(file);
    /*    ExecutorService service  = Executors.newFixedThreadPool(10);
        service.submit(new Runnable() {
            @Override
            public void run() {
                appendFile(filePath);
            }
        });
        service.awaitTermination(30, TimeUnit.SECONDS);*/

        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    appendFile(writer);
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

//        writer.close();

        readFile(filePath);
        System.out.println(fileContent);
    }
}
