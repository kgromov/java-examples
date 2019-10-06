package jenkins.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 06.10.2019.
 */
public class MainView {
    private List<Job> jobs;

    public MainView() {
        this(new ArrayList<>());
    }

    public MainView(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
