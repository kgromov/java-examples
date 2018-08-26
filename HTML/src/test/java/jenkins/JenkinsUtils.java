package jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JenkinsUtils {
    private static final Function<Long, String> BUILD_DATE = time ->
            new SimpleDateFormat("dd/MM/yyyy'T'HH-mm-ss", Locale.getDefault()).format(new Date(time));

    private static JenkinsServer jenkins;
    private static Map<String, Job> jobs;

    private JenkinsUtils() {
    }

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

    public static boolean isCrossProductBuild(String value) {
        int index = value.indexOf("REGIONS");
        if (index == -1) {
            return false;
        }
        String regions_ = value.substring(index).replace("REGIONS=", "");
        Set<String> products = Arrays.stream(regions_.split(" "))
                .filter(region -> region.contains("_"))
                .map(region -> region.substring(0, region.indexOf("_")))
                .collect(Collectors.toSet());
        System.out.println("PRODUCT: " + products);
        return products.size() > 1;
    }

    public static void findCrossProductBuilds() {
        JobWithDetails presubmitJob = getJobByName("PreSubmit");
        List<Build> presubmitsBuilds = presubmitJob.getBuilds();
        for (Build build : presubmitsBuilds) {
            int number = build.getNumber();
            getBuildLogNyNumber(presubmitJob, number).ifPresent(buildDetails ->
                    {
                        Map<String, String> parameters = buildDetails.getParameters();
                        Optional.ofNullable(parameters.get("GERRIT_CHANGE_SUBJECT")).ifPresent(parameter ->
                                {
                                    if (isCrossProductBuild(parameter)) {
                                        System.out.println("Cross product build:");
                                        System.out.println(buildDetails.getDisplayName());
                                        System.out.println(BUILD_DATE.apply(buildDetails.getTimestamp()));
                                        System.out.println(buildDetails.getUrl());
                                    }
                                }
                        );
                    }
            );
        }
    }

    public static void findBuildsByCommitMessage(Predicate<String> condition) {
        JobWithDetails presubmitJob = getJobByName("PreSubmit");
        List<Build> presubmitsBuilds = presubmitJob.getBuilds();
        for (Build build : presubmitsBuilds) {
            int number = build.getNumber();
            getBuildLogNyNumber(presubmitJob, number).ifPresent(buildDetails ->
                    {
                        Map<String, String> parameters = buildDetails.getParameters();
                        Optional.ofNullable(parameters.get("GERRIT_CHANGE_SUBJECT")).ifPresent(parameter ->
                                {
                                    if (condition.test(parameter)) {
                                        System.out.println("Commit message = " + parameter);
                                        System.out.println(buildDetails.getDisplayName());
                                        System.out.println(BUILD_DATE.apply(buildDetails.getTimestamp()));
                                        System.out.println(buildDetails.getUrl());
                                    }
                                }
                        );
                    }
            );
        }
    }

    public static JobWithDetails getJobByName(String jobName) {
        try {
            return jobs.get(jobName).details();
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("There is no job by name " + jobName);
        }
    }

    public static Optional<BuildWithDetails> getBuildLogNyNumber(String jobName, int buildNumber) {
        try {
            return Optional.ofNullable(getJobByName(jobName).getBuildByNumber(buildNumber).details());
        } catch (IOException | NullPointerException e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", jobName, buildNumber));
            System.out.println(String.format("Job '%s' does not have build by number '%d'", jobName, buildNumber));
            return Optional.empty();
        }
    }

    public static Optional<BuildWithDetails> getBuildLogNyNumber(JobWithDetails job, int buildNumber) {
        try {
            return Optional.ofNullable(job.getBuildByNumber(buildNumber).details());
        } catch (IOException | NullPointerException e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            System.out.println(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            return Optional.empty();
        }
    }

}
