package jenkins.domain;

import com.offbytwo.jenkins.model.BuildResult;
import jenkins.core.HttpClientWrapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.core.MediaType;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class BuildWithDetails extends Build {
    private List actions;
    private boolean building;
    private String description;
    private String displayName;
    private long duration;
    private long estimatedDuration;
    private String fullDisplayName;
    private String id;
    private long timestamp;
    private BuildResult result;
    /*  private List<Artifact> artifacts;
      private String consoleOutputText;
      private BuildChangeSet changeSet;
      @JsonProperty("changeSets")
      private List<BuildChangeSet> changeSets;
      private String builtOn;
      private List<BuildChangeSetAuthor> culprits;*/
    @Setter
    private HttpClientWrapper client;

    public String getConsoleLog() {
        return client.getRaw(getUrl() + "consoleText", MediaType.TEXT_PLAIN_TYPE);
    }
}
