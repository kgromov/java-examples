package oracle.speed_profiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import oracle.checker.consumers.SpeedProfilesCriteriaConsumer;
import oracle.speed_profiles.merge.HalfAverageMergeStrategy;
import oracle.speed_profiles.merge.MergeStrategy;
import oracle.speed_profiles.merge.NeighboursAverageMergeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class Aggregation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Aggregation.class);
    public static final Map<Integer, Integer> AGGREGATION_DEPTH = ImmutableMap.of(1, 4, 2, 1);
    private static final int MAX_ITERATIONS = 20;
    private static final Set<String> MARKETS = Sets.newHashSet("eu", "nar", "mrm");
    private static final Comparator<SpeedProfile> SPEED_PROFILE_COMPARATOR = Comparator.comparingInt(SpeedProfile::getAverageSpeed)
            .thenComparing(SpeedProfile::getFirstSpeed)
            .thenComparing(SpeedProfile::getLastSpeed);

    private static final IntFunction<Integer> CONSTANT_GROUP_COUNT = iterations -> 4;
    private static final IntFunction<Integer> INCREMENT_GROUP_COUNT = iterations -> 2 * (iterations + 1);

    private MergeStrategy strategy;
    private IntFunction<Integer> groupCountFunction;

    public Aggregation(MergeStrategy strategy, IntFunction<Integer> groupCountFunction) {
        this.strategy = strategy;
        this.groupCountFunction = groupCountFunction;
    }

    public List<? extends SpeedProfile> aggregateProfilesByGroups(List<? extends SpeedProfile> speedProfiles, int iterations) {
        List<SpeedProfile> aggregatedSpeedProfiles = new ArrayList<>();
        int speedProfilesAmount = speedProfiles.size();
        int groupCount = groupCountFunction.apply(iterations);
        for (int i = 0; i < speedProfilesAmount / groupCount; i++)
        {
            int startIndex = i * groupCount;
            int endIndex = (i + 1) * groupCount;
            aggregatedSpeedProfiles.addAll(getAggregationByParts(speedProfiles.subList(startIndex, endIndex)));
        }
        int leftOver = speedProfilesAmount % groupCount;
        if (leftOver != 0) {
            int startIndex = speedProfilesAmount - leftOver - 1;
            aggregatedSpeedProfiles.addAll(getAggregationByParts(speedProfiles.subList(startIndex, speedProfilesAmount)));
        }
        aggregatedSpeedProfiles.sort(SPEED_PROFILE_COMPARATOR);
        ++iterations;
        LOGGER.info(String.format("Iteration = %d, groupCount = %d, source speedProfiles size = %d, target speedProfiles size = %d",
                iterations, groupCount, speedProfiles.size(), aggregatedSpeedProfiles.size()));
        return aggregatedSpeedProfiles.size() <= 200 || iterations >= MAX_ITERATIONS
                ? aggregatedSpeedProfiles
                : aggregateProfilesByGroups(aggregatedSpeedProfiles, iterations);
    }

    private List<? extends SpeedProfile> getAggregationByParts(List<? extends SpeedProfile> subSpeedProfiles)
    {
        List<SpeedProfile> aggregatedSpeedProfiles = new ArrayList<>();
        Set<Integer> mergedIndexes = new HashSet<>(subSpeedProfiles.size());
        for (int i = 0; i < subSpeedProfiles.size(); i++) {
            if (mergedIndexes.contains(i))
            {
                continue;
            }
            SpeedProfile speedProfile1 = subSpeedProfiles.get(i).getCalibratedProfile();
            for (int j = i + 1; j < subSpeedProfiles.size() && !mergedIndexes.contains(i); j++)
            {
                if (!mergedIndexes.contains(j))
                {
                    SpeedProfile speedProfile2 = subSpeedProfiles.get(j).getCalibratedProfile();
                    Optional<? extends SpeedProfile> aggregatedSpeedProfile =
                            strategy.getAggregatedProfile(speedProfile1, speedProfile2);
                    if (aggregatedSpeedProfile.isPresent()) {
                        aggregatedSpeedProfiles.add(aggregatedSpeedProfile.get());
                        mergedIndexes.add(i);
                        mergedIndexes.add(j);
                    }
                }
            }
            if (!mergedIndexes.contains(i))
            {
                aggregatedSpeedProfiles.add(speedProfile1);
            }
        }
        return aggregatedSpeedProfiles;
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
//        MergeStrategy strategy = new HalfAverageMergeStrategy();
        MergeStrategy strategy = new NeighboursAverageMergeStrategy();
        Map<Integer, List<SpeedProfile>> speedProfilesPerSamplingId = DataProvider.extractSpeedProfiles(market);
        speedProfilesPerSamplingId.forEach((samplingId, speedProfiles) ->
        {
            LOGGER.info(String.format("########################### SAMPLING_ID = %d ###########################", samplingId));
            Aggregation aggregation = new Aggregation(strategy, INCREMENT_GROUP_COUNT);
//            Aggregation aggregation = new Aggregation(strategy, CONSTANT_GROUP_COUNT);
            List<SpeedProfile> profilesToAggregate = speedProfiles.stream()
                    .filter(profile -> profile.getMinSpeed() != profile.getMaxSpeed())
                    .sorted(SPEED_PROFILE_COMPARATOR)
                    .collect(Collectors.toList());
            List<? extends SpeedProfile> aggregatedSpeedProfiles = aggregation.aggregateProfilesByGroups(profilesToAggregate, 0);
            LOGGER.info(aggregatedSpeedProfiles.stream().map(SpeedProfile::getPatternId).collect(Collectors.toList()).toString());
            aggregation.exportAggregatedSpeedProfiles(market, aggregatedSpeedProfiles, samplingId);
        });
    }
}
