package process;

import com.sun.management.HotSpotDiagnosticMXBean;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.management.MBeanServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessUtils {

    // TODO: split to Windows and Unix
    // NOTE: currently works for Unix
    public static Set<Long> getJavaPIDs(String processName) {
        Set<Long> pids = new HashSet<>();
        try {
            System.out.println("======== Getting java process pids =========");
            String command = "pgrep " + processName;
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            Process process = builder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                try {
                    long pid = Long.parseLong(s.replaceAll("[^\\d]", ""));
                    pids.add(pid);
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return pids;
    }

    public static long getOwnPID() {
        System.out.println("======== Getting java process pid =========");
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    public static void printMemoryUsage(long pid) throws IOException {
        String command = "jmap -histo " + pid;
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
        Process process = builder.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }

    public static void captureDump() throws IOException
    {
        String command = "jmap -dump:live,format=b,file=C:\\HERE-CARDS\\my_dev_presubmits\\prod\\lane\\18139\\heap_dump.hprof " + getOwnPID();
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
        Process process = builder.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }

    private void captureDump(String dumpPath, boolean isLiveOnly) throws IOException
    {
        System.out.println("Creating dump ...");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
                server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(dumpPath, isLiveOnly);
    }

    public void captureDump(String dumpPath) throws IOException
    {
        String command = String.format("jmap -dump:live,format=b,file=%s %d", dumpPath, getOwnPID());
        System.out.println("Creating dump; command = " + command);
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
        Process process = builder.start();
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            String s;
            while ((s = stdInput.readLine()) != null)
            {
                System.out.println(s);
            }
        }
    }

    private void uploadFile()
    {
        Path path = Paths.get(".").normalize().toAbsolutePath();
        System.out.println("path = " + path);
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm_ss").format(LocalDateTime.now());
        Path dumpFilePath = path.resolve(String.format("heap_dump_%s.hprof", timestamp));
        try
        {
            captureDump(dumpFilePath.toString(), true);
            System.out.println("Attempt to upload file to s3");
            String command = String.format("aws s3 cp %s s3://akela-artifacts/profiler/dumps/%s", dumpFilePath.toString(), dumpFilePath.getFileName());
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            Process process = builder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null)
            {
                System.out.println(s);
            }
        }
        catch (IOException e)
        {
            System.err.println("Unable to create file: " + dumpFilePath + e);
        }
        finally
        {
            try
            {
                Files.deleteIfExists(dumpFilePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private static void gc()
    {
        Object obj = new Object();
        WeakReference ref = new WeakReference<>(obj);
        obj = null;
        while (ref.get() != null)
        {
            System.gc();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Start capturing dump");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
                server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(String.format("C:\\HERE-CARDS\\my_dev_presubmits\\prod\\lane\\18139\\heap_dump_%s.hprof",
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm_ss").format(LocalDateTime.now())), true);
//        captureDump();
    }
}
