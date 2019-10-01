package process;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
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
}
