package oracle.speed_profiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import oracle.checker.consumers.SpeedProfilesCriteriaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Aggregation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Aggregation.class);
    public static final Map<Integer, Integer> AGGREGATION_DEPTH = ImmutableMap.of(1, 4, 2, 1);
    private static final int MAX_ITERATIONS = 20;
    private static final Set<String> MARKETS = Sets.newHashSet("eu", "nar", "mrm");
   /* private static final Comparator<SpeedProfile> SPEED_PROFILE_COMPARATOR = Comparator.comparingInt(SpeedProfile::getPatternId)
            .thenComparing(SpeedProfile::getMinSpeed)
            .thenComparing(SpeedProfile::getMaxSpeed);*/

    private static final Comparator<SpeedProfile> SPEED_PROFILE_COMPARATOR = Comparator.comparingInt(SpeedProfile::getAverageSpeed)
            .thenComparing(SpeedProfile::getMinSpeed)
            .thenComparing(SpeedProfile::getMaxSpeed);

    private int depth;
    private List<SpeedProfile> speedProfiles;

    public Aggregation(int depth) {
        this.depth = depth;
    }

    public Aggregation(List<SpeedProfile> speedProfiles) {
        this.speedProfiles = speedProfiles;
    }

    public List<? extends SpeedProfile> aggregateProfiles(List<? extends SpeedProfile> speedProfiles, int iterations) {
        HalfAverageMerge merge = new HalfAverageMerge();
        List<SpeedProfile> aggregatedSpeedProfiles = new ArrayList<>();
        for (int i = 0; i < speedProfiles.size() / 2; i++) {
            SpeedProfile speedProfile1 = speedProfiles.get(i * 2);
            SpeedProfile speedProfile2 = speedProfiles.get(i * 2 + 1);
            Optional<? extends SpeedProfile> aggregatedSpeedProfile =
                    merge.getAggregatedProfile(speedProfile1, speedProfile2);
            if (aggregatedSpeedProfile.isPresent()) {
                aggregatedSpeedProfiles.add(aggregatedSpeedProfile.get());
            } else {
                aggregatedSpeedProfiles.add(speedProfile1);
                aggregatedSpeedProfiles.add(speedProfile2);
            }
        }
        if (speedProfiles.size() % 2 != 0) {
            aggregatedSpeedProfiles.add(speedProfiles.get(speedProfiles.size() - 1));
        }
        aggregatedSpeedProfiles.sort(SPEED_PROFILE_COMPARATOR);
        ++iterations;
        LOGGER.info(String.format("Iteration = %d, source speedProfiles size = %d, target speedProfiles size = %d",
                iterations, speedProfiles.size(), aggregatedSpeedProfiles.size()));
        return aggregatedSpeedProfiles.size() <= 200 || iterations >= MAX_ITERATIONS
                ? aggregatedSpeedProfiles
                : aggregateProfiles(aggregatedSpeedProfiles, iterations);
    }

    private void exportAggregatedSpeedProfiles(String market, List<? extends SpeedProfile> speedProfiles, int samplingId) {
        SpeedProfilesCriteriaConsumer consumer = new SpeedProfilesCriteriaConsumer(market, "");
        List<SpeedProfilesCriteriaConsumer.SpeedProfileRow> speedProfileRows = new ArrayList<>();
        speedProfiles.stream()
//                .peek(p -> LOGGER.info(String.format("PatterId = %d aggregates: %s", p.getPatternId(), p.getAggregatedPatternIDs())))
                .map(SpeedProfile::toDbRows)
                .forEach(speedProfileRows::addAll);
        // TODO: probably move to DataProvider
        Path outputFile = DataProvider.getPath("NTP_SPEED_PROFILES_AGGREGATED_s" + samplingId , market, DataProvider.Extension.SQ3);
        consumer.exportToSq3(outputFile, speedProfileRows);
    }

    public static void main(String[] args) {
        String market = "eu";
        Map<Integer, List<SpeedProfile>> speedProfilesPerSamplingId = DataProvider.extractSpeedProfiles(market);
        speedProfilesPerSamplingId.forEach((samplingId, speedProfiles) ->
        {
            LOGGER.info(String.format("########################### SAMPLING_ID = %d ###########################", samplingId));
            int depth = AGGREGATION_DEPTH.get(samplingId);
            Aggregation aggregation = new Aggregation(depth);
            List<SpeedProfile> profilesToAggregate = speedProfiles.stream()
                    .filter(profile -> profile.getMinSpeed() != profile.getMaxSpeed())
                    .sorted(SPEED_PROFILE_COMPARATOR)
                    .collect(Collectors.toList());
            List<? extends SpeedProfile> aggregatedSpeedProfiles = aggregation.aggregateProfiles(profilesToAggregate, 0);
            LOGGER.info(aggregatedSpeedProfiles.stream().map(SpeedProfile::getPatternId).collect(Collectors.toList()).toString());
            aggregation.exportAggregatedSpeedProfiles(market, aggregatedSpeedProfiles, samplingId);
        });
    }
}
