package jenkins.domain;

import jenkins.core.HttpClientWrapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.core.MediaType;

@Getter
@ToString
@EqualsAndHashCode(exclude = "client")
public class Job {
    private String name;
    protected String url;
    private String fullName;
    @Setter
    protected HttpClientWrapper client;

    public Job() {
    }

    // probably add cached one
    public JobWithDetails details()
    {
        /*JobWithDetails jobWithDetails = client.target(url)
                .path("api").path("json")
                .request(MediaType.APPLICATION_JSON_TYPE).get(JobWithDetails.class);*/
        JobWithDetails jobWithDetails = client.get(url, MediaType.APPLICATION_JSON_TYPE, JobWithDetails.class);
        jobWithDetails.setClient(client);
        return jobWithDetails;
    }
}
