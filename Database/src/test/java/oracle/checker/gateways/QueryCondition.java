package oracle.checker.gateways;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum QueryCondition {
    ALL_CONDITIONS("All_Conditions.sql"),
    LANE_CONDITIONS("Lane_Condition.sql"),
    NAV_STRAND_CONDITIONS("Nav_StrandConditions.sql"),
    SIGNPOST_CONDITIONS("Singnpost_Condition.sql"),
    TRANSITION_MASK_CONDITIONS("Transition_Mask_Condition_2.sql"),
//    TRANSITION_MASK_CONDITIONS("Transition_Mask_Condition.sql"),
    TURN_RESTRICTION_CONDITIONS("Turn_Restriction_Condition.sql");

    private String scriptName;
    private String localGatewaysQuery;
    private String stubbleGatewaysQuery;

    QueryCondition(String scriptName) {
        this.scriptName = scriptName;
        this.localGatewaysQuery = readFromFile(scriptName, "local");
        this.stubbleGatewaysQuery = readFromFile(scriptName, "stubble");
    }

    public String getScriptName() {
        return scriptName;
    }

    public String getLocalGatewaysQuery() {
        return localGatewaysQuery;
    }

    public String getStubbleGatewaysQuery() {
        return stubbleGatewaysQuery;
    }

    private static String readFromFile(String scriptName, String scriptFolder) {
        Path scriptPath = Paths.get("C:\\Projects\\java-examples\\Database\\src\\test\\java\\oracle\\scripts", scriptFolder, scriptName);
        if (!Files.exists(scriptPath)) {
            return null;
        }
        try (BufferedReader br = Files.newBufferedReader(scriptPath)) {
            StringBuilder query = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().startsWith("--")) {
                    query.append(line);
                }
            }
            return query.toString();
        } catch (IOException e) {
            return null;
        }
    }
}
