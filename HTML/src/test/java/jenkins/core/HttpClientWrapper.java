package jenkins.core;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by konstantin on 06.10.2019.
 */
public class HttpClientWrapper {
    private final Client client;
    private WebTarget baseURI;

    public HttpClientWrapper(Client client) {
        this.client = client;
    }

    public <T> T get(String path, Class<T> clazz)
    {
        return baseURI.path(path).request(MediaType.APPLICATION_JSON_TYPE).get(clazz);
    }
}
