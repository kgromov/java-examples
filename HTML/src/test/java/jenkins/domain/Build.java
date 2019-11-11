package jenkins.domain;

import jenkins.core.HttpClientWrapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.core.MediaType;

@Getter
@EqualsAndHashCode(exclude = "client")
public class Build {
    @Getter
    private int number;
//    private int queueId;
    private String url;
    @Setter
    private HttpClientWrapper client;

    public Build() {
    }

    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public BuildWithDetails details()
    {
        BuildWithDetails buildWithDetails = client.get(url, MediaType.APPLICATION_JSON_TYPE, BuildWithDetails.class);
        buildWithDetails.setClient(client);
        return buildWithDetails;
    }
    
    // TestResult
}
