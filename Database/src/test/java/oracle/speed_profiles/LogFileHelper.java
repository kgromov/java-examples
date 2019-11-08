package oracle.speed_profiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogFileHelper {
    private static final Path LOG_FOLDER = Paths.get("C:\\Projects\\java-examples\\Database\\src\\test\\java\\oracle\\output\\logs");

    private Path logPath;

    public LogFileHelper(String strategyName, int timePeriod) {
        logPath = LOG_FOLDER.resolve(strategyName + "_" + timePeriod + "h");
        try {
            Files.deleteIfExists(logPath);
            Files.createFile(logPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message)
    {
        try {
            Files.write(logPath, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
