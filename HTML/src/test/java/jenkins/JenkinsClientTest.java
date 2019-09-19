package jenkins;

import jenkins.forkjoinpool.BuildInfo;
import jenkins.forkjoinpool.JobInfoTask;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by konstantin on 11.03.2018.
 */
public class JenkinsClientTest {
    private Settings settings;

    @BeforeTest
    public void initSettings() {
        this.settings = Settings.getInstance();
        System.out.println(settings);
    }

    @BeforeMethod
    private void checkSettings() {
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
        jobInfo.getDownstreamBuilds().forEach(System.out::println);
        // check for consoleOutput and downstreamJobs
        Report.copyTemplates();
        Report.writeToReport(jobInfo);
        // convert to sq3
        Convertor.convertToSqLite(settings, jobInfo);
    }

    @Test(enabled = true)
    public void checkBuild_task() {
//        JenkinsUtils.authenticate("http://akela-dev.nds.ext.here.com:8080/", "kgromov", "Here1501!");
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        // provide here jobName and build via parameters
        BuildInfo buildInfo = new BuildInfo(settings.getJobName(), settings.getBuildNumber());

        JobInfoTask task = new JobInfoTask(buildInfo);
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(task);
        do {
            System.out.printf("Main: Thread Count:%d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal:%d\n", pool.getStealCount());
            System.out.printf("Main: Parallelism:%d\n", pool.getParallelism());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());
        pool.shutdown();
        // log all jobs info
        System.out.println(buildInfo);
        buildInfo.getDownstreamBuilds().forEach(System.out::println);
        // check for consoleOutput and downstreamJobs
        Report.copyTemplates();
        Report.writeToReport(buildInfo);
        // convert to sq3
        Convertor.convertToSqLite(settings, buildInfo);
    }

    @Test(enabled = false)
    public void listCrossProductBuilds() {
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        JenkinsUtils.findCrossProductBuilds();
    }

    @Test(enabled = true)
    public void listRegionBuilds() {
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        JenkinsUtils.findBuildsByCommitMessage(message -> message.contains("LC_") && message.contains("KEEPUSER"));
    }


    @Test(enabled = true)
    public void listFreeBuilds() throws IOException, InterruptedException {
        JenkinsUtils.authenticate(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        JenkinsUtils.logCompiledBuilds(10, 3, "PreSubmit");
    }

    @Test
    public void checkBuldTimer() {
        new BuildsDiffer(settings.getJobName()).collectBuildTimeTrend(settings);
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
