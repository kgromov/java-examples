package jenkins.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

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
    private List<Job> downstreamProjects;
    private List<Job> upstreamProjects;
    @Setter
    private Client client;
    private String url;
    private Map<Integer, Build> buildNumberToBuild;

    public JobWithDetails() {
    }

    public Build getBuildByNumber(int buildNumber)
    {
        return buildNumberToBuild.containsKey(buildNumber)
                ? buildNumberToBuild.get(buildNumber)
                : client.target(url).path("job").path(String.valueOf(buildNumber))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Build.class);
    }

    public Build getLatestBuild()
    {
        return null;
    }

    public List<Build> getLastBuilds()
    {
        return null;
    }

    public List<Build> getAllBuilds()
    {
        return null;
    }
}
