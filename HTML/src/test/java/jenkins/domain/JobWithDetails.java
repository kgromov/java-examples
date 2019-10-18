package jenkins.domain;

import com.google.common.collect.ImmutableMap;
import jenkins.core.HttpClientWrapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(exclude = {"buildNumberToBuild", "client"})
public class JobWithDetails {
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
    @Setter
    private HttpClientWrapper client;
    private String url;
    private Map<Integer, Build> buildNumberToBuild;

    public JobWithDetails() {
        this.buildNumberToBuild = builds.stream()
                .peek(build -> build.setClient(client))
                .collect(Collectors.toMap(Build::getNumber, b -> b));
    }

    public Build getBuildByNumber(int buildNumber)
    {
        Build build = buildNumberToBuild.get(buildNumber);
        if (build == null)
        {
            build = client.get(url, MediaType.APPLICATION_JSON_TYPE, Build.class);
            buildNumberToBuild.put(buildNumber, build);
        }
        return build;
    }

    public Build getLatestBuild()
    {
        return builds.get(0);
    }

    public List<Build> getLastBuilds()
    {
        return builds;
    }

    public List<Build> getAllBuilds()
    {
        return client.get(url,
                MediaType.APPLICATION_JSON_TYPE,
                ImmutableMap.of("tree", "allBuilds[number[*],url[*],queueId[*]]"),
                Builds.class).getBuilds();
    }
}
