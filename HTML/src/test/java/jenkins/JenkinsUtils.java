package jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

public class JenkinsUtils {
    private static JenkinsServer jenkins;
    private static Map<String, Job> jobs;

    private JenkinsUtils(){}

    public static void authenticate(String url, String login, String password) {
        try {
//            jenkins = new JenkinsServer(new URI("http://akela-dev.nds.ext.here.com:8080/"), "kgromov", "Here1501!");
            jenkins = new JenkinsServer(new URI(url), login, password);
            jobs = jenkins.getJobs();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(String.format("Unable to login to jenkins:" +
                    " %s with credentials: %s/%s", url, login, password), e);
        }
    }

    public static JobWithDetails getJobByName(String jobName) {
        try {
            return jobs.get("Compile").details();
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("There is no job by name " + jobName);
        }
    }

    public static Optional< BuildWithDetails> getBuildLogNyNumber(String jobName, int buildNumber) {
        try {
            return Optional.ofNullable(getJobByName(jobName).getBuildByNumber(buildNumber).details());
        } catch (IOException e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", jobName, buildNumber));
            return Optional.empty();
        }
    }

    public static Optional< BuildWithDetails> getBuildLogNyNumber(JobWithDetails job, int buildNumber) {
        try {
            return Optional.ofNullable(job.getBuildByNumber(buildNumber).details());
        } catch (IOException e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            return Optional.empty();
        }
    }

}
