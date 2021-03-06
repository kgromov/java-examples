package jenkins.core;

import com.offbytwo.jenkins.model.BuildResult;
import javafx.util.Pair;
import jenkins.JenkinsUtils;
import jenkins.domain.Build;
import jenkins.domain.BuildWithDetails;
import jenkins.domain.JobWithDetails;
import jenkins.forkjoinpool.BuildInfo;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobInfoNew extends BuildInfo {
    private static final String BUILD_PREFIX = "\\s*#%d";
    private static final Pattern JOB_BUILD = Pattern.compile("(Compile|Merge|Validation_Suite|Compile Market|City_FTS|Nuance)\\s*#\\d+");
    private static final Pattern PATH_TO_RESULT_PATTERN = Pattern.compile(".*(s3://akela-artifacts).*");
    private static final String PATH_TO_RESULT = "s3://"; // and aws and ticket (auto) and .sq3
    private static final Predicate<String> IS_FOLDER = path -> path.charAt(path.length() - 1) == '/';
    // patters
    private final static String NEW_STRING_RECORD_PATTERN = "\\d{2,4}-\\d{2}-\\d{2}\\s?\\d{2}:\\d{2}:\\d{2}(,\\d{3})?";
    private final static Pattern EXCEPTION_PATTERN = Pattern.compile(".+Exception[^\\n]++(\\s+at .++)+");
    @Setter
    private JenkinsClient client;


    public JobInfoNew(String jobName, int buildNumber) {
        super(jobName, buildNumber);
    }

    public void collectDownStreamJobs() {
        JobWithDetails jobWithDetails = client.getJobs().get(jobName).details();
        Optional<BuildWithDetails> build = Optional.ofNullable(jobWithDetails.getBuildByNumber(buildNumber)).map(Build::details);
        build.ifPresent(info ->
        {
            this.buildName = info.getDisplayName(); // or set with jobName ?
            this.buildTime = TimeUnit.SECONDS.convert(info.getDuration(), TimeUnit.MILLISECONDS);
            this.timestamp = info.getTimestamp();
            this.parameters = info.getParameters();
            this.result = info.getResult();
            this.isPassed = result == BuildResult.SUCCESS;
            this.exceptions = new LinkedHashSet<>();
            // consoleOutput
            try {
                String consoleOutput = info.getConsoleLog();
                BigDecimal decimal = BigDecimal.valueOf(consoleOutput.getBytes().length);
                this.logSize = decimal.divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP).floatValue();
//                this.downstreamBuilds = new ArrayList<>();
                this.downstreamBuilds = Collections.synchronizedList(new ArrayList<>());
                // parse db credentials
                if (isCredentialsApplicable()) {
                    this.dbCredentials = JenkinsUtils.getCredentials(consoleOutput);
                }
                // parse consoleOutput
                parseConsoleLog(consoleOutput).parallelStream()
                        .map(job -> new JobInfoNew(job.getKey(), job.getValue()))
                        .peek(job -> job.setClient(client))
                        .peek(job -> downstreamBuilds.add(job))
                        .forEach(JobInfoNew::collectDownStreamJobs);
            } catch (Exception e) {
                throw new RuntimeException(String.format("ConsoleOutput of job's '%s', " +
                        "build '%d' could not be provided", jobName, buildNumber), e);
            }
        });
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
      /*  // parse for exceptions
        Arrays.stream(consoleOutput.split(NEW_STRING_RECORD_PATTERN))
                .filter(row -> row.contains("Exception:"))
                .forEach(row ->
                {
                    Matcher matcher1 = EXCEPTION_PATTERN.matcher(row);
                    if(matcher1.find())
                    {
                        exceptions.add(matcher1.group());
                    }
                });*/
        return jobs;
    }
}
