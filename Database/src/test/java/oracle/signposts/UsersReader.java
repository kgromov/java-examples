package oracle.signposts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class UsersReader {
    private static final String RESOURCES_FILE = "C:\\Projects\\java-examples\\Database\\src\\test\\java\\oracle\\signposts\\resources\\%s_users.txt";
    private String market;
    private Set<String> cdcUsers;

    public UsersReader(String market) {
        this.market = market;
    }

    public Set<String> getCdcUsers() {
        if (cdcUsers != null) {
            return cdcUsers;
        }
        try {
            return cdcUsers = Files.readAllLines(Paths.get(String.format(RESOURCES_FILE, market))).stream()
                    .filter(line -> !line.startsWith("//"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
