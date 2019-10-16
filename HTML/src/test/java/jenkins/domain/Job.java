package jenkins.domain;

import jenkins.core.HttpClientWrapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        /*JobWithDetails jobWithDetails = client.target(url)
                .path("api").path("json")
                .request(MediaType.APPLICATION_JSON_TYPE).get(JobWithDetails.class);*/
        Response response = client.target(url).path("api").path("json").request(MediaType.APPLICATION_JSON_TYPE).get();
        JobWithDetails jobWithDetails = new HttpClientWrapper(client).unmarshal(response.readEntity(String.class), JobWithDetails.class);
        jobWithDetails.setClient(client);
        return jobWithDetails;
    }
}
