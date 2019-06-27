package aws;

import aws.fillspec.UpdateRegion;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UpdateRegionHelper {

    public static Set<UpdateRegionDummy> getUpdateRegions() {
        Set<UpdateRegionDummy> updateRegions = new HashSet<>(UpdateRegion.values().length);
        for (UpdateRegion region : UpdateRegion.values()) {
            int id = region.getUrId();
            String[] names = region.name().split("_");
            String name = names.length > 2 ? IntStream.range(0, names.length - 1).boxed()
                    .map(i -> names[i])
                    .collect(Collectors.joining("_"))
                    : names[0];
            region.getUpdateRegionSets().stream()
                    .map(Enum::name)
                    .map(setName -> setName.split("_")[0])
                    .map(market -> new UpdateRegionDummy(id, name, market))
                    .forEach(updateRegions::add);
        }
        return updateRegions;
    }

    @Data
    public static class UpdateRegionDummy {
        private final int id;
        private final String name;
        private final String market;
    }
}
