package jenkins.domain;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BuildWithDetails {
    private Client client;
    private int buildNumber;

    public String getConsoleLog()
    {
        Response response = client.target("http://10.126.87.186:8080/job/Stable_With_Meta_Data/" + buildNumber + "/consoleText")
                .request(MediaType.TEXT_PLAIN).get();
        return response.readEntity(String.class);
    }
}
