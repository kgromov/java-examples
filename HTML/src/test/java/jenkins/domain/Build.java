package jenkins.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

@Getter
@EqualsAndHashCode(exclude = "client")
public class Build {
    private int number;
//    private int queueId;
    private String url;
    @Setter
    private Client client;

    public Build() {
    }

    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public BuildWithDetails details()
    {
        BuildWithDetails buildWithDetails = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get(BuildWithDetails.class);;
        buildWithDetails.setClient(client);
        return buildWithDetails;
    }
    
    // TestResult
}
