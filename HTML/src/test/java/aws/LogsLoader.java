package aws;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LogsLoader {
    public static void main(String[] args) {
        Map<String, Set<Integer>> updateRegionsInMarket = new HashMap<>();
        Map<String, UpdateRegionHelper.UpdateRegionDummy> updateRegionDummyMap = new HashMap<>();
        UpdateRegionHelper.getUpdateRegions().forEach(u -> updateRegionDummyMap.put(u.getName(), u));
        try (
                InputStreamReader aInputStreamReader = new FileReader(new File("C:\\HERE-CARDS\\my_dev_presubmits\\14823\\191E0\\" +
                        "ACCUMULATED_ERRORS_LANE_GROUPS_VALIDITY_RANGE_VALIDATIONcontain Overlap.csv"));
                CSVReader reader = new CSVReader(aInputStreamReader)) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                String urName = row[0].split(";")[0];
                UpdateRegionHelper.UpdateRegionDummy updateRegion = updateRegionDummyMap.get(urName);
                updateRegionsInMarket.computeIfAbsent(updateRegion.getMarket(), updateRegionIDs -> new TreeSet<>()).add(updateRegion.getId());
            }
            System.out.println(updateRegionsInMarket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
