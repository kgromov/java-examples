package jenkins.domain;

import java.util.List;

public class AllBuilds {
    private List<Build> allBuilds;

    public AllBuilds() {
    }

    public List<Build> getAllBuilds() {
        return this.allBuilds;
    }
}
