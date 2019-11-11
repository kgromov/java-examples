package jenkins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.offbytwo.jenkins.model.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by konstantin on 06.10.2019.
 */
public class MainView {
//    @JsonIgnore
    private String _class;
    private List<Job> jobs;
    private List<View> views;

    public MainView() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public MainView(List<Job> jobs, List<View> views) {
        this.jobs = jobs;
        this.views = views;
    }

    public MainView(Job... jobs) {
        this(Arrays.asList(jobs), new ArrayList<>());
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }
}
