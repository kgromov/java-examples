package jenkins;

import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobInfo {
    // "\\w{4,}\\s*#\\d+"
    private static final Pattern JOB_BUILD = Pattern.compile("[^\\d]{4,}\\s*#\\d+"); // or strictly specify job names
    private static final String BUILD_PREFIX = "\\s*#%d";
    //    private static final Pattern JOB_BUILD = Pattern.compile("(Compile|Merge|Validation_Suite)\\s*#\\d+");
    private static final String PATH_TO_RESULT = "s3://"; // and aws and ticket (auto) and .sq3
    // patters
//    private final static Pattern EXCEPTION_PATTERN = Pattern.compile("Exception\\b:");
    private final static Pattern EXCEPTION_PATTERN = Pattern.compile("Caused\\s?by\\b:");
    // to build composite pattern
    private List<JobInfo> downstreamJobs;
    // fields
    private String buildName;
    private int buildNumber;
    private int logSize; // round to bigger part, change type to float:
    // NumberFormat.getInstance().format(Float.valueOf(consoleOutput.length())/1024) or via BigDecimal
    private BuildResult result;
    private boolean isPassed;  // like double check
    private String pathToS3;
    private Set<String> exceptions;

    // to parse console output
    public JobInfo(String consoleOutput) {
        // consoleOutput
        this.logSize = consoleOutput.getBytes().length / 1024;
        this.downstreamJobs = new ArrayList<>();
        // parse consoleOutput
        parseConsoleLog(consoleOutput).forEach(job ->
                downstreamJobs.add(new JobInfo(job.getKey(), job.getValue()))
        );
    }

    // to get job directly from Jenkins
    public JobInfo(String jobName, int buildNumber) {
        this.buildNumber = buildNumber;
        Optional<BuildWithDetails> build = JenkinsUtils.getBuildLogNyNumber(jobName, buildNumber);
        build.ifPresent(info ->
        {
            this.buildName = info.getDisplayName(); // or set with jobName ?
            this.result = info.getResult();
            this.isPassed = result == BuildResult.SUCCESS;
            // consoleOutput
            try {
                String consoleOutput = info.getConsoleOutputText();
                this.logSize = consoleOutput.getBytes().length / 1024;
                this.downstreamJobs = new ArrayList<>();
                // parse consoleOutput
                parseConsoleLog(consoleOutput).forEach(job ->
                        downstreamJobs.add(new JobInfo(job.getKey(), job.getValue()))
                );
            } catch (IOException e) {
                throw new RuntimeException(String.format("ConsoleOutput of job's '%s', " +
                        "build '%d' could not be provided", jobName, buildNumber), e);
            }
        });
    }

    public List<JobInfo> getDownstreamJobs() {
        return downstreamJobs;
    }

    /**
     * Method parse job condoleOutput to collect downstream jobs
     * and set the following fields:
     * 1) pathToS3      - .sq3 file with test results;
     * 2) exceptions    - collect all exceptions while occurred in log;
     * 3) isPassed      - could change its value if exceptions present or any test fails (for automation job)
     *
     * @param consoleOutput - console output of current job
     * @return list of downstream jobs in form: JobName + buildNumber
     */
    private List<Pair<String, Integer>> parseConsoleLog(String consoleOutput) {
        List<Pair<String, Integer>> jobs = new ArrayList<>();
        Matcher matcher = JOB_BUILD.matcher(consoleOutput);
        while (matcher.find()) {
            String build = matcher.group();
            try {
                int buildNumber = Integer.valueOf(build.replaceAll("[^\\d]", ""));
                String jobName = build.replaceAll(String.format(BUILD_PREFIX, buildNumber), "");
                jobs.add(new Pair<>(jobName, buildNumber));
            } catch (NumberFormatException ignored) {
            }
        }
        return jobs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildName, buildNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof JobInfo)) {
            return false;
        }
        JobInfo that = (JobInfo) o;
        return this.buildName.equalsIgnoreCase(that.buildName)
                && this.buildNumber == that.buildNumber;
    }

    @Override
    public String toString() {
        return "JobInfo{" +
                "downstreamJobs=" + downstreamJobs +
                ", buildName='" + buildName + '\'' +
                ", buildNumber=" + buildNumber +
                ", logSize=" + logSize +
                ", result=" + result +
                ", pathToS3='" + pathToS3 + '\'' +
                ", isPassed=" + isPassed +
                '}';
    }
}
