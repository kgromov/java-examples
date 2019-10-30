package jenkins.domain;

import com.google.common.collect.ImmutableMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.Range;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
public class JobWithDetails extends Job {
    private String description;
    private String displayName;
    private boolean buildable;
    private List<Build> builds;
    private Build firstBuild;
    private Build lastBuild;
    private Build lastCompletedBuild;
    private Build lastFailedBuild;
    private Build lastStableBuild;
    private Build lastSuccessfulBuild;
    private Build lastUnstableBuild;
    private Build lastUnsuccessfulBuild;
    private int nextBuildNumber;
    private boolean inQueue;
    //    private QueueItem queueItem;
//    private List<Job> downstreamProjects;
//    private List<Job> upstreamProjects;
    private final static Map<Integer, Build> BUILD_NUMBER_TO_BUILD = new HashMap<>();

    public JobWithDetails() {
        this(new ArrayList<>());
    }

    public JobWithDetails(List<Build> builds) {
        this.builds = builds;
        setBuilds(builds);
    }

    public Build getBuildByNumber_(int buildNumber) {
        Build build = BUILD_NUMBER_TO_BUILD.computeIfAbsent(buildNumber,
                b -> client.get(url + buildNumber, MediaType.APPLICATION_JSON_TYPE, Build.class));
        build.setClient(client);
        return build;
    }

    public Build getBuildByNumber(int buildNumber) {
        Build build = BUILD_NUMBER_TO_BUILD.get(buildNumber);
        if (build == null)
        {
            List<Build> builds = getAllBuilds();
            setBuilds(builds);
            build = BUILD_NUMBER_TO_BUILD.get(buildNumber);
        }
        Optional.ofNullable(build).ifPresent(b -> b.setClient(client));
        return build;
    }

    public List<Build> getLastBuilds() {
        return builds;
    }

    // FIXME
    public List<Build> getAllBuilds() {
        List<Build> builds = client.get(url,
                MediaType.APPLICATION_JSON_TYPE,
                ImmutableMap.of("tree", "allBuilds[number[*],url[*],queueId[*]]"),
                Builds.class).getBuilds();
        setBuilds(builds);
        return builds;
    }

    public List<Build> getAllBuilds(Range<Integer> range) {
        return null;
    }

    public synchronized void setBuilds(List<Build> builds) {
        builds.stream()
                .peek(b -> b.setClient(client))
                .forEach(build -> BUILD_NUMBER_TO_BUILD.put(build.getNumber(), build));
    }
}
