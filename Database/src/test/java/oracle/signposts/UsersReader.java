package oracle.signposts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class UsersReader {
    private static final String RESOURCES_FILE = "C:\\Projects\\java-examples\\Database\\src\\test\\java\\oracle\\signposts\\resources\\%s_users.txt";
    private static final Set<String> SAMPLE_USERS = new UsersReader("sample").getCdcUsers();
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

    public Set<String> getCdcUserWithDVN(String customDVN) {
        Set<String> cdcUsers = getCdcUsers();
        String anyUser = cdcUsers.iterator().next();
        String configurationDVN = anyUser.substring(anyUser.lastIndexOf('_') + 1);
        return cdcUsers.stream()
                .map(user -> user.replace(configurationDVN, customDVN))
                .collect(Collectors.toSet());
    }

    public static Set<String> withoutSampleUsers(Set<String> cdcUsers)
    {
       return cdcUsers.stream()
               .filter(user -> !SAMPLE_USERS.contains(user.substring(user.lastIndexOf('_') + 1)))
               .collect(Collectors.toSet());
    }
}
