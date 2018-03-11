package jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by konstantin on 11.03.2018.
 */
public class JenkinsClientTest {
    public static void main(String[] args) throws URISyntaxException, IOException {
        JenkinsServer jenkins = new JenkinsServer(new URI("http://127.0.0.1:8080/"), "admin", "admin");
        Map<String, Job> jobs = jenkins.getJobs();
        try {
            JobWithDetails jobDetails = jobs.get("hello").details();
            Build lastBuild = jobDetails.getLastBuild();
            /*lastBuild.getTestResult();
            lastBuild.getTestReport();*/

            BuildWithDetails buildWithDetails = lastBuild.details();
            buildWithDetails.getParameters();
            buildWithDetails.getConsoleOutputText();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
