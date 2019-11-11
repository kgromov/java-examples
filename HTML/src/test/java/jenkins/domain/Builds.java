package jenkins.domain;

import java.util.ArrayList;
import java.util.List;

public class Builds {
    private List<Build> builds;

    public Builds() {
        this(new ArrayList<>());
    }

    public Builds(List<Build> builds) {
        this.builds = builds;
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }
}
