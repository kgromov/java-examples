package jenkins.core;


import jenkins.domain.Job;
import jenkins.domain.MainView;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 06.10.2019.
 */
public class JenkinsClient {
    private HttpClientWrapper client;
    private final String url;
    private final String user;
    private final String password;

    public JenkinsClient(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(user, password);
        Client client = ClientBuilder.newBuilder()
                .register(auth)
                .build();
        this.client = new HttpClientWrapper(client);
    }

    // or add generic wrapper response logic: {check response status; log raw; serialize via mapper}
    public Map<String, Job> getJobs() {
//        Response response = client.target(url).path("api").path("json").request(MediaType.APPLICATION_JSON_TYPE).get();
//        MainView mainView = response.readEntity(MainView.class);
        MainView mainView = client.get(url, MediaType.APPLICATION_JSON_TYPE, MainView.class);
        return mainView.getJobs().stream()
                .peek(job -> job.setClient(client))
                .collect(Collectors.toMap(Job::getName, e -> e));

        /*return Maps.uniqueIndex(jobs, new Function() {
            public String apply(Job job) {
                job.setClient(JenkinsServer.this.client);
                return job.getName();
            }
        });*/
    }

    // Probably AutoClosable?
    public void close() {
        client.close();
    }
}
