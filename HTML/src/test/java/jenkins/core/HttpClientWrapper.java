package jenkins.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by konstantin on 06.10.2019.
 */
public class HttpClientWrapper {
    private final Client client;
    private WebTarget baseURI;
    private ObjectMapper mapper;

    public HttpClientWrapper(Client client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public HttpClientWrapper(WebTarget baseURI) {
        this.client = null;
        this.baseURI = baseURI;
        this.mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public <T> T get(String path, Class<T> clazz)
    {
        return baseURI.path(path).request(MediaType.APPLICATION_JSON_TYPE).get(clazz);
    }

    public synchronized <T> T get(String url, MediaType mediaType, Class<T> clazz)
    {
        String requestedURI  = mediaType == MediaType.APPLICATION_JSON_TYPE
                ? Paths.get(url).resolve("api").resolve("json").toString()
                : url;
        Response response = client.target(url).request(mediaType).get();
        String rawJson = response.readEntity(String.class);
        try {
            T item = mapper.readValue(rawJson, clazz);
            return item;
        } catch (IOException e) {
            int a =1;
            throw new RuntimeException(String.format("Unable to deserialize %s to class = %s", rawJson, clazz), e);
        }
    }
}
