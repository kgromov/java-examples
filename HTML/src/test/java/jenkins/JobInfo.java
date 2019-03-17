package jenkins;

import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import javafx.util.Pair;
import jenkins.forkjoinpool.BuildInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobInfo extends BuildInfo {
    // "\\w{4,}\\s*#\\d+"
//    private static final Pattern JOB_BUILD = Pattern.compile("[^\\d]{4,}\\s*#\\d+"); // or strictly specify job names
    private static final String BUILD_PREFIX = "\\s*#%d";
    private static final Pattern JOB_BUILD = Pattern.compile("(Compile|Merge|Validation_Suite|Compile Market)\\s*#\\d+");
    //    private static final Pattern PATH_TO_RESULT_PATTERN = Pattern.compile("^.*(s3://akela-artifacts).*$"); // (s3://akela-artifacts).*$
    private static final Pattern PATH_TO_RESULT_PATTERN = Pattern.compile(".*(s3://akela-artifacts).*"); // (s3://akela-artifacts).*$
    private static final String PATH_TO_RESULT = "s3://"; // and aws and ticket (auto) and .sq3
    private static final Predicate<String> IS_FOLDER = path -> path.charAt(path.length() - 1) == '/';
    // patters
//    private final static Pattern EXCEPTION_PATTERN = Pattern.compile("Exception\\b:");
    private final static Pattern EXCEPTION_PATTERN = Pattern.compile("(?s)(?<=Exception occured:).*?(?=Caused)");
    // to build composite pattern
    private List<BuildInfo> downstreamJobs;

    // to parse console output
    public JobInfo(String consoleOutput) {
        // consoleOutput
        super(null, -1);
        BigDecimal decimal = BigDecimal.valueOf(consoleOutput.getBytes().length);
        this.logSize = decimal.divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP).floatValue();
        this.downstreamJobs = new ArrayList<>();
        // parse consoleOutput
        parseConsoleLog(consoleOutput).forEach(job ->
                downstreamJobs.add(new JobInfo(job.getKey(), job.getValue()))
        );
    }

    // to get job directly from Jenkins
    public JobInfo(String jobName, int buildNumber) {
        super(jobName, buildNumber);
        this.jobName = jobName;
        this.buildNumber = buildNumber;
        Optional<BuildWithDetails> build = JenkinsUtils.getBuildLogByNumber(jobName, buildNumber);
        build.ifPresent(info ->
        {
            this.buildName = info.getDisplayName(); // or set with jobName ?
            this.buildTime = TimeUnit.SECONDS.convert(info.getDuration(), TimeUnit.MILLISECONDS);
            this.parameters = info.getParameters();
            this.result = info.getResult();
            this.isPassed = result == BuildResult.SUCCESS;
            this.exceptions = new LinkedHashSet<>();
            // consoleOutput
            try {
                String consoleOutput = info.getConsoleOutputText();
                BigDecimal decimal = BigDecimal.valueOf(consoleOutput.getBytes().length);
                this.logSize = decimal.divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP).floatValue();
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

    public List<BuildInfo> getDownstreamJobs() {
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
        System.out.println(String.format("Parse consoleOutput of job: name=%s, displayName=%s, build=%d", jobName, buildName, buildNumber));
        // downstream jobs
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
        // parse for result files/folders
        try {
            String searchIn = consoleOutput.substring(consoleOutput.lastIndexOf(PATH_TO_RESULT));
            matcher = PATH_TO_RESULT_PATTERN.matcher(searchIn);
            while (matcher.find()) {
                String path = matcher.group();
                this.pathToS3 = IS_FOLDER.test(path) ? path : path.substring(0, path.lastIndexOf('/'));
            }
        } catch (IndexOutOfBoundsException e) {

        }
        // parse for exceptions
        matcher = EXCEPTION_PATTERN.matcher(consoleOutput);
        while (matcher.find()) {
            exceptions.add(matcher.group());
        }
        return jobs;
    }
}
