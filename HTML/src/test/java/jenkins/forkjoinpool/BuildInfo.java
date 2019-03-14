package jenkins.forkjoinpool;

import com.offbytwo.jenkins.model.BuildResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildInfo {
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
    private List<BuildInfo> downstreamBuilds = new ArrayList<>();

    public BuildInfo(String jobName, int buildNumber) {
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

    public Set<String> getExceptions() {
        return exceptions;
    }

    public boolean isFound() {
        return result != null;
    }

    public List<BuildInfo> getDownstreamBuilds() {
        return downstreamBuilds;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public void setLogSize(float logSize) {
        this.logSize = logSize;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public void setResult(BuildResult result) {
        this.result = result;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public void setPathToS3(String pathToS3) {
        this.pathToS3 = pathToS3;
    }

    public void setExceptions(Set<String> exceptions) {
        this.exceptions = exceptions;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildName, buildNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BuildInfo)) {
            return false;
        }
        BuildInfo that = (BuildInfo) o;
        return this.buildName.equalsIgnoreCase(that.buildName)
                && this.buildNumber == that.buildNumber;
    }

    @Override
    public String toString() {
        return "BuildInfo{" +
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
