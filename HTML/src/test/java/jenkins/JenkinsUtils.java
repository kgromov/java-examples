package jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.helper.Range;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JenkinsUtils {
    private static final long TIMEOUT = 10 * 60 * 1000;
    private static final Function<Long, String> BUILD_DATE = time ->
            new SimpleDateFormat("dd/MM/yyyy'T'HH-mm-ss", Locale.getDefault()).format(new Date(time));

    private static final String BUILD_PREFIX = "\\s*#%d";
    private static final Pattern JOB_BUILD = Pattern.compile("(Compile|Compile Market)\\s*#\\d+");
    private final static Pattern DB_CREDENTIALS_PATTERN = Pattern.compile("(SourceDBUser|DBUser|DBServer)=\\w+");

    private static JenkinsServer jenkins;
    private static Map<String, Job> jobs;

    private JenkinsUtils() {
    }

    public static void authenticate(String url, String login, String password) {
        try {
//            jenkins = new JenkinsServer(new URI("http://akela-dev.nds.ext.here.com:8080/"), "kgromov", "Here1501!");
            jenkins = new JenkinsServer(new URI(url), login, password);
            /*HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setConnectionManager(new PoolingHttpClientConnectionManager());  // + set max per route
            JenkinsHttpClient client = new JenkinsHttpClient(uri, builder, username, password);
            JenkinsServer jenkins = new JenkinsServer(client);*/
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
            getBuildLogByNumber(presubmitJob, number).ifPresent(buildDetails ->
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
        try {
            presubmitJob.getAllBuilds();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Build> presubmitsBuilds = presubmitJob.getBuilds();
        for (Build build : presubmitsBuilds) {
            int number = build.getNumber();
            getBuildLogByNumber(presubmitJob, number).ifPresent(buildDetails ->
                    {
                        Map<String, String> parameters = buildDetails.getParameters();
                        Optional.ofNullable(parameters.get("GERRIT_CHANGE_SUBJECT")).ifPresent(parameter ->
                                {
                                    if (condition.test(parameter)) {
                                        System.out.println("Commit message = " + parameter);
                                        System.out.println(buildDetails.getDisplayName());
                                        System.out.println(BUILD_DATE.apply(buildDetails.getTimestamp()));
                                        System.out.println(buildDetails.getUrl());
                                        // downstream jobs are required
                                        try {
                                            String consoleOutput = buildDetails.getConsoleOutputText();
                                            Matcher matcher = JOB_BUILD.matcher(consoleOutput);
                                            while (matcher.find()) {
                                                String downstreamBuild = matcher.group();
                                                try {
                                                    int buildNumber = Integer.valueOf(downstreamBuild.replaceAll("[^\\d]", ""));
                                                    String jobName = downstreamBuild.replaceAll(String.format(BUILD_PREFIX, buildNumber), "");
                                                    getBuildLogByNumber(jobName, buildNumber).ifPresent(details ->
                                                            {
                                                                try {
                                                                    System.out.println(getCredentials(details.getConsoleOutputText()));
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                    );
                                                } catch (NumberFormatException ignored) {
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                    }
            );
        }
    }

    // 2 nav_strands expected, but it only has
    public static void findBuildsByConsoleLog(Predicate<String> condition) {
        JobWithDetails presubmitJob = getJobByName("PreSubmit");
        List<Build> presubmitsBuilds = presubmitJob.getBuilds();
        for (Build build : presubmitsBuilds) {
            int number = build.getNumber();
            getBuildLogByNumber(presubmitJob, number).ifPresent(buildDetails ->
                    {
                        // downstream jobs are required
                        try {
                            String consoleOutput = buildDetails.getConsoleOutputText();
                            Matcher matcher = JOB_BUILD.matcher(consoleOutput);
                            while (matcher.find()) {
                                String downstreamBuild = matcher.group();
                                try {
                                    int buildNumber = Integer.valueOf(downstreamBuild.replaceAll("[^\\d]", ""));
                                    String jobName = downstreamBuild.replaceAll(String.format(BUILD_PREFIX, buildNumber), "");
                                    getBuildLogByNumber(jobName, buildNumber).ifPresent(details ->
                                            {
                                                try {
                                                    String downStreamConsoleOutput = buildDetails.getConsoleOutputText();
                                                    if (condition.test(downStreamConsoleOutput)) {
                                                        System.out.println(buildDetails.getDisplayName());
                                                        System.out.println(BUILD_DATE.apply(buildDetails.getTimestamp()));
                                                        System.out.println(buildDetails.getUrl());
                                                        System.out.println(getCredentials(downStreamConsoleOutput));
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                    );
                                } catch (NumberFormatException ignored) {
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    public static DbCredentials getCredentials(String consoleOutput) {
        Matcher matcher = DB_CREDENTIALS_PATTERN.matcher(consoleOutput);
        String sourceDbUser = null;
        String dbUser = null;
        String dBServer = null;
        while (matcher.find()) {
            String[] paramAndValue = matcher.group().split("=");
            String paramName = paramAndValue[0];
            switch (paramName) {
                case "SourceDBUser":
                    sourceDbUser = sourceDbUser == null ? paramAndValue[1] : sourceDbUser;
                    break;
                case "DBUser":
                    dbUser = dbUser == null ? paramAndValue[1] : dbUser;
                    break;
                case "DBServer":
                    if (dBServer == null) {
                        String[] temp = consoleOutput.substring(matcher.start(), matcher.start() + 100).split("\\,");
                        String[] params = temp[0].split("=");
                        dBServer = params[1];
                    }
                    break;
                default:
                    break;
            }
            if (sourceDbUser != null && dbUser != null && dBServer != null) {
                break;
            }
        }
        return new DbCredentials(sourceDbUser, dbUser, dBServer);
    }


    public static void logCompiledBuilds(int maxActive, int compiled, String jobName) throws IOException, InterruptedException {
        JobWithDetails presubmitJob = getJobByName(jobName);
        Range buildRange = Range.build().from(0).to(maxActive);
        List<Build> presubmitsBuilds = presubmitJob.getAllBuilds(buildRange);
        // filter out only not finished builds
        leaveRunningBuilds(presubmitsBuilds);
        AtomicInteger finished = new AtomicInteger();
        AtomicLong elapsedTime = new AtomicLong();
        while (finished.get() == 0 || elapsedTime.get() < TIMEOUT) {
            boolean isNotCompletedCompilation = true;
            for (Build build : presubmitsBuilds) {
                BuildWithDetails buildWithDetails = build.details();
                isNotCompletedCompilation = buildWithDetails.getResult() != null
                        // http://akela-prd.nds.ext.here.com:8080/job/PreSubmit/409/logText/progressiveText
//                        || !buildWithDetails.getConsoleOutputText().contains("Reactor Summary:");
                        // but should be: http://akela-prd.nds.ext.here.com:8080/job/PreSubmit/409/consoleFull
                        || !getFullConsoleLog(buildWithDetails).contains("Reactor Summary:");
                if (isNotCompletedCompilation) {
                    System.out.println(BUILD_DATE.apply(System.nanoTime()) + "\tNot finished compilation: " + build.getUrl());
                    break;
                }
                System.out.println(BUILD_DATE.apply(System.nanoTime()) + "\tFinished compilation: " + build.getUrl());
            }
            if (!isNotCompletedCompilation) {
                Build lastBuild = presubmitJob.getLastBuild();
                boolean isNewBuilds = presubmitsBuilds.get(0).getNumber() != lastBuild.getNumber();
                if (!isNewBuilds) {
                    break;
                }
                System.out.println(BUILD_DATE.apply(System.nanoTime()) + "\tAdded new build: " + lastBuild.getUrl());
            }
            presubmitsBuilds = presubmitJob.getAllBuilds(buildRange);
            leaveRunningBuilds(presubmitsBuilds);
            long iteration = 30 * 1000L;
            elapsedTime.addAndGet(iteration);
            Thread.sleep(30 * 1000);
        }
    }

    private static String getFullConsoleLog(BuildWithDetails buildWithDetails) throws IOException {
        return buildWithDetails.getClient().get(buildWithDetails.getUrl() + "consoleFull");
    }

    private static void leaveRunningBuilds(List<Build> builds) throws IOException {
        Iterator<Build> iterator = builds.iterator();
        while (iterator.hasNext()) {
            Build build = iterator.next();
            if (build.details().getResult() != null) {
                iterator.remove();
            }
        }
    }


    public static JobWithDetails getJobByName(String jobName) {
        try {
            return jobs.get(jobName).details();
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("There is no job by name " + jobName);
        }
    }

    public static Optional<BuildWithDetails> getBuildLogByNumber(String jobName, int buildNumber) {
        try {
            return buildNumber > 100 ? getBuildLogByNumberEx(getJobByName(jobName), buildNumber)
                    : Optional.ofNullable(getJobByName(jobName).getBuildByNumber(buildNumber).details());
        } catch (IOException | NullPointerException e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", jobName, buildNumber));
            System.out.println(String.format("Job '%s' does not have build by number '%d'", jobName, buildNumber));
            return Optional.empty();
        }
    }

    public static Optional<BuildWithDetails> getBuildLogByNumber(JobWithDetails job, int buildNumber) {
        try {
            return Optional.ofNullable(job.getBuildByNumber(buildNumber).details());
        } catch (IOException | NullPointerException e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            System.out.println(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            return Optional.empty();
        }
    }

    public static Optional<BuildWithDetails> getBuildLogByNumberEx(JobWithDetails job, int buildNumber) {
        try {
            int lastBuildNumber = job.getLastBuild().getNumber();
            int diff = lastBuildNumber - buildNumber;
            Range buildRange = Range.build().from(0).to(diff + 1);
            List<Build> presubmitsBuilds = job.getAllBuilds(buildRange);
            return Optional.ofNullable(presubmitsBuilds.get(diff).details());
        } catch (Exception e) {
//            throw new RuntimeException(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            System.out.println(String.format("Job '%s' does not have build by number '%d'", job.getDisplayName(), buildNumber));
            return Optional.empty();
        }
    }

    private String getResponseByURLConnection(String url, Map<String, String> map) {
        long start = System.currentTimeMillis();
        StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false);
            HttpURLConnection.setFollowRedirects(false);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("getResponseByURLConnection for url '" + url + "'= " + (System.currentTimeMillis() - start));
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to get response by url\t%s\ncause\n%s", url, e.getMessage()));
        } finally {
            System.out.println(String.format("getResponseByURLConnection for url '%s', time = '%d'", url, (System.currentTimeMillis() - start)));
            System.out.println("Response\t" + response);
        }

    }
}
