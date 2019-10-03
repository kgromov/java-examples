package jenkins.domain;

import java.util.List;
import java.util.Map;

public class JobWithDetails {

    private Map<Integer, Build> builds;

    Build getBuildByNumber(int buildNumber)
    {
        return builds.get(buildNumber);
    }

    Build getLatestBuild()
    {
        return null;
    }

    List<Build> getLastBuilds()
    {
        return null;
    }

    List<Build> getAllBuilds()
    {
        return null;
    }
}
