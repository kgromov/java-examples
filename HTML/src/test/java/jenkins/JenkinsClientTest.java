package jenkins;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by konstantin on 11.03.2018.
 */
public class JenkinsClientTest {
    private static final String DEFAULT_MY_PROPERTIES = "templates/build_config.properties";
    private Settings settings;

    @BeforeTest
    public void initSettings() {
        String pathToProperties = System.getProperty("my.properties", DEFAULT_MY_PROPERTIES);
        if (pathToProperties != null && !pathToProperties.isEmpty()) {
            Properties prop = new Properties();
            try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_MY_PROPERTIES)) {
//                prop.load(new FileReader(new File(pathToProperties)));
                prop.load(is);
                this.settings = new Settings(prop);
                System.out.println(settings);
            } catch (IOException ignored) {
            }
        }
    }

    @BeforeMethod
    private void checkSettings()
    {
        if (settings == null) {
            throw new RuntimeException("Settings were not instantiated cause " +
                    "of incorrect file or file format passed by " + System.getProperty("my.properties"));
        }
    }

    @Test(enabled = true)
    public void checkBuild() {
//        JenkinsUtils.authenticate("http://akela-dev.nds.ext.here.com:8080/", "kgromov", "Here1501!");
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        // provide here jobName and build via parameters
        JobInfo jobInfo = new JobInfo(settings.getJobName(), settings.getBuildNumber());
        // log all jobs info
        System.out.println(jobInfo);
        jobInfo.getDownstreamJobs().forEach(System.out::println);
        // check for consoleOutput and downstreamJobs
        Report.copyTemplates();
        Report.writeToReport(jobInfo);
    }

    @Test(enabled = false)
    public void listCrossProductBuilds()
    {
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        JenkinsUtils.findCrossProductBuilds();
    }

    @Test(enabled = false)
    public void listRegionBuilds()
    {
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        JenkinsUtils.findBuildsByCommitMessage(message -> message.contains("DEU_G1_NO") && message.contains("KEEPUSER"));
    }


    public static void main(String[] args) throws IOException {
        File log = new File("C:\\Users\\kgromov\\Desktop\\jenkinsAPI\\consoleText_dev_fail.txt");
        BufferedReader reader = new BufferedReader(new FileReader(log));
        String inputLine;
        StringBuilder buffer = new StringBuilder();
        // read file - ability to operate indexes
        while ((inputLine = reader.readLine()) != null) {
//            buffer.append(inputLine).append("\n");
            buffer.append(inputLine);
        }
        String consoleLog = buffer.toString();
        int index = consoleLog.lastIndexOf("s3://");
        String sub = consoleLog.substring(index);
    }

}