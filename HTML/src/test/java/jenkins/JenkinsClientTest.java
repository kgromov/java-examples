package jenkins;

import jenkins.core.JenkinsClient;
import jenkins.core.JobInfoNew;
import jenkins.domain.Build;
import jenkins.domain.BuildWithDetails;
import jenkins.domain.Job;
import jenkins.domain.JobWithDetails;
import jenkins.extensions.BuildsDiffer;
import jenkins.extensions.Convertor;
import jenkins.forkjoinpool.BuildInfo;
import jenkins.forkjoinpool.JobInfoTask;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
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

    @Test
    public void checkNewImplementation() {
        JenkinsClient jenkinsClient = new JenkinsClient(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        Map<String, Job> jobs = jenkinsClient.getJobs();
        Job presubmit = jobs.get(settings.getJobName());
        JobWithDetails jobWithDetails = presubmit.details();
        Build build = jobWithDetails.getBuildByNumber(31565);
        BuildWithDetails buildWithDetails = build.details();
        buildWithDetails.getConsoleLog();
        jenkinsClient.close();
    }

    @Test(enabled = true)
    public void checkBuild_new() {
        JenkinsClient client = new JenkinsClient(settings.getJenkinsURL(), settings.getLogin(), settings.getPassword());
        JobInfoNew jobInfo = new JobInfoNew(settings.getJobName(), settings.getBuildNumber());
        jobInfo.setClient(client);
        jobInfo.collectDownStreamJobs();
        // log all jobs info
        System.out.println(jobInfo);
        jobInfo.getDownstreamBuilds().forEach(System.out::println);
        // check for consoleOutput and downstreamJobs
        Report.copyTemplates();
        Report.writeToReport(jobInfo);
       // convert to sq3
        Convertor.convertToSqLite(jobInfo);
        // append to build time trend
//        new BuildsDiffer().appendToTrend(jobInfo);
    }

   /* @Test
    public void anotherAttemptToLogin()
    {
        HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(settings.getLogin(), settings.getPassword());
        Client client = ClientBuilder.newBuilder()
                .register(auth)
                .build();
        IntStream.range(331, 432).boxed()
                .parallel()
                .forEach(buildNumber ->
                {
                    Response response = client.target("http://10.126.87.186:8080/job/Stable_With_Meta_Data/"+buildNumber+"/consoleText")
                            .request(MediaType.TEXT_PLAIN).get();
                    String responseText = response.readEntity(String.class);
                    System.out.println(String.format("Build#%d has status = %d and length = %d",
                            buildNumber, response.getStatus(), responseText.length()));

                });
    }*/

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
        Convertor.convertToSqLite(jobInfo);
        // append to build time trend
        new BuildsDiffer().appendToTrend(jobInfo);
    }

    @Test(enabled = false)
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
        Convertor.convertToSqLite(buildInfo);
        // append to build time trend
        new BuildsDiffer().appendToTrend(buildInfo);
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
        new BuildsDiffer().collectBuildTimeTrend();
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
