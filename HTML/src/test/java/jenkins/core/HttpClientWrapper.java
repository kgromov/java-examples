package jenkins.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * Created by konstantin on 06.10.2019.
 */
public class HttpClientWrapper {
    private final Logger logger = LoggerFactory.getLogger(HttpClientWrapper.class);
    private final Client client;
    private WebTarget baseURI;
    private ObjectMapper mapper;

    public HttpClientWrapper(Client client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
     /*   mapper.disable(FAIL_ON_NULL_CREATOR_PROPERTIES);
        mapper.disable(FAIL_ON_NULL_FOR_PRIMITIVES);*/
    }

    public HttpClientWrapper(WebTarget baseURI) {
        this.client = null;
        this.baseURI = baseURI;
        this.mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /*public <T> T get(String path, MediaType mediaType, Class<T> clazz)
    {
        return client.target(path).request(mediaType).get(clazz);
    }*/

    public String getRaw(String path, MediaType mediaType)
    {
        return client.target(path).request(mediaType).get(String.class);
    }

    // probably use UriBuilder
    public synchronized <T> T get(String url, MediaType mediaType, Class<T> clazz)
    {
        URI requestedURI  = mediaType == MediaType.APPLICATION_JSON_TYPE
                ? UriBuilder.fromPath(url).path("api").path("json").build()
                : UriBuilder.fromPath(url).build();
        Response response = client.target(requestedURI).request(mediaType).get();
        String rawJson = response.readEntity(String.class);
        try {
            return mapper.readValue(rawJson, clazz);
        } catch (IOException e) {
            logger.error(response.toString());
            throw new RuntimeException(String.format("Unable to deserialize %s to class = %s", rawJson, clazz), e);
        }
    }

    public synchronized <T> T getDirectly(String url, MediaType mediaType, Class<T> clazz)
    {
        Response response = client.target(url).request(mediaType).get();
        String rawJson = response.readEntity(String.class);
        try {
            return mapper.readValue(rawJson, clazz);
        } catch (IOException e) {
            logger.error(response.toString());
            throw new RuntimeException(String.format("Unable to deserialize %s to class = %s", rawJson, clazz), e);
        }
    }

    public synchronized <T> T get(String url, MediaType mediaType, Map<String, String> queryParams, Class<T> clazz)
    {
        UriBuilder builder = mediaType == MediaType.APPLICATION_JSON_TYPE
                ? UriBuilder.fromPath(url).path("api").path("json")
                : UriBuilder.fromPath(url);
        queryParams.forEach(builder::queryParam);
        URI requestedURI = builder.build();
       /* WebTarget webTarget = client.target(requestedURI);
        queryParams.forEach(webTarget::queryParam);
        Response response = webTarget.request(mediaType).get();*/
        Response response = client.target(requestedURI).request(mediaType).get();
        String rawJson = response.readEntity(String.class);
        try {
            return mapper.readValue(rawJson, clazz);
        } catch (IOException e) {
            logger.error(response.toString());
            throw new RuntimeException(String.format("Unable to deserialize %s to class = %s", rawJson, clazz), e);
        }
    }

    public void close() {
        client.close();
    }
}
