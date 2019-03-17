package jenkins.forkjoinpool;

import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import javafx.util.Pair;
import jenkins.JenkinsUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JobInfoTask extends RecursiveAction {
    // patters
    private static final String BUILD_PREFIX = "\\s*#%d";
    private static final Pattern JOB_BUILD = Pattern.compile("(Compile|Merge|Validation_Suite|Compile Market)\\s*#\\d+");
    private static final Pattern PATH_TO_RESULT_PATTERN = Pattern.compile(".*(s3://akela-artifacts).*"); // (s3://akela-artifacts).*$
    private static final String PATH_TO_RESULT = "s3://"; // and aws and ticket (auto) and .sq3
    private static final Predicate<String> IS_FOLDER = path -> path.charAt(path.length() - 1) == '/';
    private final static Pattern EXCEPTION_PATTERN = Pattern.compile("(?s)(?<=Exception occured:).*?(?=Caused)");

    private BuildInfo buildInfo;

    public JobInfoTask(BuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    @Override
    protected void compute() {
        Optional<BuildWithDetails> build = JenkinsUtils.getBuildLogByNumber(buildInfo.getJobName(), buildInfo.getBuildNumber());
        build.ifPresent(info ->
        {
            String buildName = info.getDisplayName(); // or set with jobName ?
            long buildTime = TimeUnit.SECONDS.convert(info.getDuration(), TimeUnit.MILLISECONDS);
            Map<String, String> parameters = info.getParameters();
            BuildResult result = info.getResult();
            boolean isPassed = result == BuildResult.SUCCESS;
            Set<String> exceptions = new LinkedHashSet<>();

            buildInfo.setBuildName(buildName);
            buildInfo.setBuildTime(buildTime);
            buildInfo.setParameters(parameters);
            buildInfo.setResult(result);
            buildInfo.setPassed(isPassed);
            buildInfo.setExceptions(exceptions);
            // consoleOutput
            try {
                String consoleOutput = info.getConsoleOutputText();
                BigDecimal decimal = BigDecimal.valueOf(consoleOutput.getBytes().length);
                float logSize = decimal.divide(BigDecimal.valueOf(1024), 2, RoundingMode.HALF_UP).floatValue();
                buildInfo.setLogSize(logSize);
                // parse consoleOutput
                List<JobInfoTask> tasks = parseConsoleLog(consoleOutput, buildInfo).stream()
                        .map(job -> new BuildInfo(job.getKey(), job.getValue()))
                        .peek(buildInfo.getDownstreamBuilds()::add)
                        .map(JobInfoTask::new)
                        .collect(Collectors.toList());

                // try to change with count (if downstream jobs > 10) then fork else process
//                invokeAll(tasks);
                for(JobInfoTask task : tasks)
                {
                    try {
                        Thread.sleep(100);
                        task.invoke();
//                        task.get();
                        buildInfo.getDownstreamBuilds().add(task.getBuildInfo());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } /*catch (ExecutionException e) {
                        e.printStackTrace();
                    }*/
                }

               /* parseConsoleLog(consoleOutput, buildInfo).forEach(job ->
                        downstreamBuilds.add(new BuildInfo(job.getKey(), job.getValue()))
                );*/
            } catch (IOException e) {
                throw new RuntimeException(String.format("ConsoleOutput of job's '%s', " +
                        "build '%d' could not be provided", buildInfo.getJobName(), buildInfo.getBuildNumber()), e);
            }
        });
    }

    private List<Pair<String, Integer>> parseConsoleLog(String consoleOutput, BuildInfo buildInfo) {
        System.out.println(String.format("Parse consoleOutput of job: name=%s, displayName=%s, build=%d",
                buildInfo.getJobName(), buildInfo.getBuildName(), buildInfo.getBuildNumber()));
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
                String pathToS3 = IS_FOLDER.test(path) ? path : path.substring(0, path.lastIndexOf('/'));
                buildInfo.setPathToS3(pathToS3);
            }
        } catch (IndexOutOfBoundsException e) {

        }
        // parse for exceptions
        matcher = EXCEPTION_PATTERN.matcher(consoleOutput);
        while (matcher.find()) {
            buildInfo.getExceptions().add(matcher.group());
        }
        return jobs;
    }

}
