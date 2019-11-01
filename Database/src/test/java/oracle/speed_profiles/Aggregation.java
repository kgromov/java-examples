package oracle.speed_profiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import oracle.checker.consumers.SpeedProfilesCriteriaConsumer;
import oracle.speed_profiles.merge.HalfAverageMergeStrategy;
import oracle.speed_profiles.merge.MergeStrategy;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.stream.IntStream;

public class Aggregation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Aggregation.class);
//    public static final Map<Integer, Integer> AGGREGATION_DEPTH = ImmutableMap.of(1, 4, 2, 1);
    public static final Map<Integer, Integer> AGGREGATION_DEPTH = ImmutableMap.of(1, 8, 2, 2, 4, 1);
    private static final Set<String> MARKETS = Sets.newHashSet("eu", "nar", "mrm");
   /* private static final Comparator<SpeedProfile> SPEED_PROFILE_COMPARATOR = Comparator.comparingInt(SpeedProfile::getAverageSpeed)
            .thenComparing(SpeedProfile::getAverageDaySpeed)
            .thenComparing(SpeedProfile::getAverageNightSpeed)
            .thenComparing(SpeedProfile::getMinSpeed)
            .thenComparing(SpeedProfile::getMaxSpeed);*/

    private static final Comparator<SpeedProfile> SPEED_PROFILE_COMPARATOR = Comparator.comparingInt(SpeedProfile::getAverageDaySpeed)
            .thenComparing(SpeedProfile::getAverageNightSpeed)
            .thenComparing(SpeedProfile::getAverageSpeed)
            .thenComparing(SpeedProfile::getMinSpeed)
            .thenComparing(SpeedProfile::getMaxSpeed);

    private static final IntFunction<Integer> CONSTANT_GROUP_COUNT = iterations -> 4;
    private static final IntFunction<Integer> INCREMENT_GROUP_COUNT = iterations -> 2 * (iterations + 1);

    private MergeStrategy strategy;
    private IntFunction<Integer> groupCountFunction;

    public Aggregation(MergeStrategy strategy, IntFunction<Integer> groupCountFunction) {
        this.strategy = strategy;
        this.groupCountFunction = groupCountFunction;
    }

    public List<? extends SpeedProfile> aggregateProfiles(List<? extends SpeedProfile> speedProfiles, int iterations) {
        List<SpeedProfile> aggregatedSpeedProfiles = new ArrayList<>();
        int speedProfilesAmount = speedProfiles.size();
        int groupCount = groupCountFunction.apply(iterations);
        for (int i = 0; i < speedProfilesAmount / groupCount; i++)
        {
            int startIndex = i * groupCount;
            int endIndex = (i + 1) * groupCount;
            aggregatedSpeedProfiles.addAll(getMergedGroup(speedProfiles.subList(startIndex, endIndex)));
        }
        int leftOver = speedProfilesAmount % groupCount;
        if (leftOver != 0) {
            int startIndex = speedProfilesAmount - leftOver - 1;
            aggregatedSpeedProfiles.addAll(getMergedGroup(speedProfiles.subList(startIndex, speedProfilesAmount)));
        }
        // as merge could lead to the same speed profile
        aggregatedSpeedProfiles = aggregatedSpeedProfiles.stream()
                .sorted(SPEED_PROFILE_COMPARATOR)
                .distinct()
                .collect(Collectors.toList());
        ++iterations;
        LOGGER.info(String.format("Iteration = %d, groupCount = %d, source speedProfiles size = %d, target speedProfiles size = %d",
                iterations, groupCount, speedProfiles.size(), aggregatedSpeedProfiles.size()));
        return aggregatedSpeedProfiles.size() <= 200 || aggregatedSpeedProfiles.size() == speedProfilesAmount
                ? aggregatedSpeedProfiles
                : aggregateProfiles(aggregatedSpeedProfiles, iterations);
    }

    private List<? extends SpeedProfile> getMergedGroup(List<? extends SpeedProfile> subSpeedProfiles)
    {
        List<SpeedProfile> aggregatedSpeedProfiles = new ArrayList<>();
        Set<Integer> mergedIndexes = new HashSet<>(subSpeedProfiles.size());
        for (int i = 0; i < subSpeedProfiles.size(); i++) {
            if (mergedIndexes.contains(i))
            {
                continue;
            }
//            SpeedProfile speedProfile1 = subSpeedProfiles.get(i).getCalibratedProfile();
            SpeedProfile speedProfile1 = subSpeedProfiles.get(i);
            for (int j = i + 1; j < subSpeedProfiles.size() && !mergedIndexes.contains(i); j++)
            {
                if (!mergedIndexes.contains(j))
                {
//                    SpeedProfile speedProfile2 = subSpeedProfiles.get(j).getCalibratedProfile();
                    SpeedProfile speedProfile2 = subSpeedProfiles.get(j);
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

    private List<? extends SpeedProfile> getTopUsedSpeedProfiles(List<? extends SpeedProfile> speedProfiles,
                                                                 Map<Integer, Integer> speedProfilesUsage)
    {
        return speedProfiles.stream()
                .map(s -> Pair.of(s, s.getAggregatedPatternIDs().stream()
                                .mapToInt(p -> speedProfilesUsage.getOrDefault(p, 0)).sum()))
                .sorted(Comparator.comparingInt(Pair::getRight))
                .map(Pair::getLeft)
                .limit(200)
                .collect(Collectors.toList());
    }

    private void exportAggregatedSpeedProfiles(String market, List<? extends SpeedProfile> speedProfiles, int samplingId) {
        SpeedProfilesCriteriaConsumer consumer = new SpeedProfilesCriteriaConsumer(market, "");
        List<SpeedProfilesCriteriaConsumer.SpeedProfileRow> speedProfileRows = new ArrayList<>();
        speedProfiles.stream()
                .peek(p -> LOGGER.info(String.format("PatterId = %d aggregates: %s", p.getPatternId(), p.getAggregatedPatternIDs())))
                .map(SpeedProfile::toDbRows)
                .forEach(speedProfileRows::addAll);
        // TODO: probably move to DataProvider
        Path outputFile = DataProvider.getPath("NTP_SPEED_PROFILES_AGGREGATED_s" + samplingId , market, DataProvider.Extension.SQ3);
        consumer.exportToSq3(outputFile, speedProfileRows);
    }

    public static void main(String[] args) {
        String market = "eu";
        Map<Integer, List<SpeedProfile>> speedProfilesPerSamplingId = DataProvider.extractSpeedProfiles(market);
        Map<Integer, Map<Integer, Integer>> speedProfilesUsagePerSampleId = DataProvider.getSpeedProfilesUsage(market);
        speedProfilesPerSamplingId.forEach((samplingId, speedProfiles) ->
        {
            LOGGER.info(String.format("########################### SAMPLING_ID = %d ###########################", samplingId));
            Map<Integer, Integer> speedProfilesUsage = speedProfilesUsagePerSampleId.get(samplingId);
            MergeStrategy strategy = new HalfAverageMergeStrategy(speedProfilesUsage);
//        MergeStrategy strategy = new NeighboursAverageMergeStrategy();
            Aggregation aggregation = new Aggregation(strategy, INCREMENT_GROUP_COUNT);
//            Aggregation aggregation = new Aggregation(strategy, CONSTANT_GROUP_COUNT);
            List<SpeedProfile> profilesToAggregate = speedProfiles.stream()
                    .filter(profile -> profile.getMinSpeed() != profile.getMaxSpeed())
                    .map(SpeedProfile::getCalibratedProfile)
                    .sorted(SPEED_PROFILE_COMPARATOR)
                    .collect(Collectors.toList());
            List<? extends SpeedProfile> aggregatedSpeedProfiles = aggregation.aggregateProfiles(profilesToAggregate, 0);
             aggregatedSpeedProfiles = aggregation.getTopUsedSpeedProfiles(aggregatedSpeedProfiles, speedProfilesUsage);
            LOGGER.info(aggregatedSpeedProfiles.stream().map(SpeedProfile::getPatternId).collect(Collectors.toList()).toString());
//            aggregation.exportAggregatedSpeedProfiles(market, aggregatedSpeedProfiles, samplingId);
        });
    }
}
