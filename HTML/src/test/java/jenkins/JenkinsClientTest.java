package jenkins;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by konstantin on 11.03.2018.
 */
public class JenkinsClientTest {
    private String jobName;
    private int buildNumber;

    @Test
    // TODO: pass properties file as parameter and then read its properties  - so create init method
    public void checkBuild() {
        JenkinsUtils.authenticate("http://akela-dev.nds.ext.here.com:8080/", "kgromov", "Here1501!");
        // provide here jobName and build via parameters
        JobInfo jobInfo = new JobInfo(jobName, buildNumber);
        // check for consoleOutput and downstreamJobs
        Report.writeJobsInfoToXml(jobInfo);
    }

    public static void main(String[] args) throws IOException {
        // read consoleLog to test
//        File log = new File("D:\\\\workspace\\\\konstantin-examples\\\\HTML\\\\src\\\\main\\\\resources\\\\jenkins_logs\\consoleText_auto_fail.txt");
        File log = new File("D:\\workspace\\konstantin-examples\\HTML\\src\\main\\resources\\jenkins_logs\\consoleText_dev_fail.txt");
        BufferedReader reader = new BufferedReader(new FileReader(log));
        String inputLine;
        StringBuilder buffer = new StringBuilder();
        // read file - ability to operate indexes
        while ((inputLine = reader.readLine()) != null) {
//            buffer.append(inputLine).append("\n");
            buffer.append(inputLine);
        }
        JobInfo jobInfo = new JobInfo(buffer.toString());
    }
}
