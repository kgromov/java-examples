package oracle.checker.readers;

import java.util.Set;

public interface UserReader {
    String CDC_USER_PATTERN = "CDCA_%s_%s";

    static String convertToDbUserWithDVN(String region, String dvn)
    {
        return String.format(CDC_USER_PATTERN, region, dvn);
    }

    Set<String> getCdcUsers();
}
