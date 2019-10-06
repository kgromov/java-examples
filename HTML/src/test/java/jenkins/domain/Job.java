package jenkins.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

@Getter
@ToString
@EqualsAndHashCode(exclude = "client")
public class Job {
    private String name;
    private String url;
    private String fullName;
    @Setter
    private Client client;

    public Job() {
    }

    // probably add cached one
    public JobWithDetails details()
    {
        JobWithDetails jobWithDetails = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get(JobWithDetails.class);;
        jobWithDetails.setClient(client);
        return jobWithDetails;
    }
}
