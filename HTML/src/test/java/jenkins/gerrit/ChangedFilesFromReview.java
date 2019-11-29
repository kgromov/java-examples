package jenkins.gerrit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import jenkins.Settings;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class ChangedFilesFromReview {
    private static final  Logger LOGGER = LoggerFactory.getLogger(ChangedFilesFromReview.class);
    private static final String GET_REQUEST_TEMPLATE = "https://gerrit.it.here.com/a/changes/%s/revisions/%s/files/";
    private static final  Map<String, String> CHANGE_ID_TO_REVISION_ID = new ImmutableMap.Builder<String, String>()
            .put("Icfbdac81b5417dd19b4d80d01aba25f2db9ad0f6", "07ffde571d1fbb3f1b739334a1fe39aa96376a7b") // 1) POI (5 files)
            .put("Ie5ecbc06bf11c1e1a578a5fdeed2e44cbad0c052", "e372b1c36cdb95777896f9efb3863962872112bd") // 2) NDSAKELA-16804 link/stubble geometry (2 files)
            .put("I659871aee130341e6f8b3ed0fa06cfd64da2220c", "09cdbe28f2980992cc1535a14ee4c6e8bfb81804") // 3) NDSAKELA-16862 Add new extended global gateways table
            .put("I5f8116ab27c5c6ea64fa18cce346358bce42e795", "d55d86c8212018925c0f2b53606c68b271909690") // 4) VERIFY_STUBBLE_LINK_NECESSITY - Conditions + NavStrands (11 files)
            .put("I95e8cb687c1f0d22b327e6dd4f3db3832e9846f2", "0ade19a3c26ac21a209fcc69e5070fffcff0ed94") // 5) NDSAKELA-14796 + NDSAKELA-13387 (8 files)
            .put("I4ff1314b4badb6a71d83964d664ec4d341ea65af", "fbf2fd28677815b1b60ff2c1a0e5c5ee11f55a3e") // 6) NDSAKELA-16307 Shift u-turn restriction flags from node-level to link-level (10 files)
            .put("Ib55a3aaeca3809ed75cdd19d32802219050e7ddb", "ec60907842670296448d67c8196d3745ccb2692a") // 7) NDSAKELA-15535 Remove the "single" gateways on higher levels where the connection is absent (7 files)
            .put("Ifde0ba2a8e64bfccfb13995273af0a437477a0d1", "6c930ac502a0f8478d9b444944d6a651795f0f52") // 8) NDSAKELA-15722 Add stubble nodes to PREV_IDs (1 file)
            .put("Idd3984939daa0898fbe434ddd391b39a7c5de208", "f80b1dbb7dc4f2a5af2dd96ae35b7eeda2234e5d") // 9) NDSAKELA-13756 [DEV](HERE_QA.256.0.15862) PRODUCT_ROUTING_VERIFY_COMMON_NODES_ARE_GATEWAYS VS test failed (2 files)
            .put("Id39d9c04fe88bdd315e4f8b05a88df1a65890db1", "03a2eb99fe2419248b70c0e16fb53157802b65f4") // 10) NDSAKELA-14796 [DEV](HERE_QA.256.0.10148) PRODUCT_ROUTING_VERIFY_BORDER_NODES_TRANSITION_MASK VS test failed (3 files)
            .put("I5ea4526581fd98f27bf2029c8bb2c1d84df760de", "0addd5adf5d6408401c7306f4f415386f65513ab") // 11) NDSAKELA-14796 [DEV](HERE_QA.256.0.10148) PRODUCT_ROUTING_VERIFY_BORDER_NODES_TRANSITION_MASK VS test failed (test new DVN) (6 files)
            .put("If64560b42a74af7ca5e5d4771f197c16a86031da", "b31aca1d25b8ea0c857172f02995232b98a4576e") // 12) NDSAKELA-14539 (KPM - start TM changes, 20 files)
            .build();

    private static final  Map<String, String> CHANGES_AFTER_C84 = ImmutableMap.of(
            "Ib122a448e1440bd2c7f5c01ee85f9b77db9405bf", "ab481c31ad30028eb45843a0516c01634f05b981", // NDSAKELA-17536 [DEV](HERE_QA.256.0.21807) PRODUCT_ROUTING_VERIFY_STUBBLES
            "I794af8fdb0069b9e0abf6e0b9d1f1bcc0b15a1ce", "41285528f78b371a174b69f6e23e65cc42830691" // NDSAKELA-14690 Global gateway table counterparts (1 file)
    );

    private enum Status
    {
        CREATED("A"),
        DELETED("D"),
        UPDATED("U"),
        UNDEFINED("");

        private String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Status getStatus(String statusValue)
        {
           return Arrays.stream(values()).filter(v -> v.getValue().equals(statusValue)).findFirst().orElse(UPDATED);
        }
    }

    public static void main(String[] args) {
        // https://gerrit.it.here.com/a/changes/1135174/detail    - to retrieve patchsets/s3 files etc
        // https://gerrit.it.here.com/a/changes/?q=1135174&o=CURRENT_REVISION&o=CURRENT_COMMIT
        Settings settings = Settings.getInstance();
        HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(settings.getLogin(), settings.getPassword());
        Client client = ClientBuilder.newBuilder()
                .register(auth)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);

        Map<Status, Set<String>> changedFiles = new EnumMap<>(Status.class);

        CHANGE_ID_TO_REVISION_ID.forEach((changeId, commit) ->
        {
            System.out.println();
            LOGGER.info(String.format("ChangeId = %s, commit = %s", changeId, commit));
            String response = client.target(String.format(GET_REQUEST_TEMPLATE, changeId, commit))
                    .request(MediaType.APPLICATION_JSON)
                    .get().readEntity(String.class);
            response = response.replace( ")]}'\n", "");
            try {
                JsonNode node = mapper.readTree(response);
                Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
                while (iterator.hasNext())
                {
                    Map.Entry<String, JsonNode> childNode = iterator.next();
                    String  fileName = childNode.getKey();
                    boolean isModified = !childNode.getValue().has("status");
                    String statusValue = isModified ? "U" : childNode.getValue().get("status").asText();
                    Status status = Status.getStatus(statusValue);
                    changedFiles.computeIfAbsent(status, files -> new TreeSet<>()).add(fileName);
                    LOGGER.debug(String.format("Key = %s, value = %s", childNode.getKey(), childNode.getValue()));
                }
            /*    System.out.println("\nVia map");
                Map<String, Object> map = mapper.readValue(response, new TypeReference<Map<String,Object>>(){});
                map.forEach((key, value) -> System.out.println(String.format("Key = %s, value = %s", key, value)));*/
            } catch (IOException e) {
                LOGGER.error("", e);
            }
        });
        LOGGER.info(changedFiles.toString());
    }
}
