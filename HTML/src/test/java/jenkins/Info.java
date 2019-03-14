package jenkins;

import com.offbytwo.jenkins.model.BuildResult;
import jenkins.forkjoinpool.BuildInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Info {
    // fields
    protected String buildName;
    protected String jobName;
    protected int buildNumber;
    protected float logSize;
    protected long buildTime;
    protected BuildResult result;
    protected boolean isPassed;  // like double check
    protected String pathToS3;
    protected Set<String> exceptions;
    protected Map<String, String> parameters;

    // to build composite pattern
    protected List<BuildInfo> downstreamBuilds = new ArrayList<>();

    public Info(String jobName, int buildNumber) {
        this.jobName = jobName;
        this.buildNumber = buildNumber;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getJobName() {
        return jobName;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public float getLogSize() {
        return logSize;
    }

    public String getResult() {
        return result.name();
    }

    public boolean isPassed() {
        return isPassed;
    }

    public String getPathToS3() {
        return pathToS3;
    }

    public String getParameters() {
        return parameters == null ? "" : parameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\r\n"));
    }

    public String getResultExceptions() {
        return exceptions == null ? "" : exceptions.stream().collect(Collectors.joining("\r\n")); // or tokenize in a proper wqy in xslt
    }

    public boolean isFound() {
        return result != null;
    }

    public List<BuildInfo> getDownstreamBuilds() {
        return downstreamBuilds;
    }

    @Override
    public String toString() {
        return "JobInfo{" +
                ", buildName='" + buildName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", buildNumber=" + buildNumber +
                ", logSize=" + logSize +
                ", buildTime=" + buildTime +
                ", result=" + result +
                ", isPassed=" + isPassed +
                ", pathToS3='" + pathToS3 + '\'' +
                ", exceptions=" + exceptions +
                ", parameters=" + parameters +
                '}';
    }
}
