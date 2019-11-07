package oracle.speed_profiles.merge;

import oracle.speed_profiles.AggregatedSpeedProfile;
import oracle.speed_profiles.SpeedProfile;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

public class HalfAverageRangeMergeStrategy implements MergeStrategy {
    private int threshold = MergeStrategy.DEFAULT_THRESHOLD;
    private int rangeThreshold = threshold * 2;

    @Override
    public Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2)
    {
        if (!profile1.isMergeable(profile2, threshold))
        {
            return Optional.empty();
        }
        List<Integer> speedPerTime1 = profile1.getSpeedPerTime();
        List<Integer> speedPerTime2 = profile2.getSpeedPerTime();

        int aggregatedPatternId = profile1.getUsages() >=profile2.getUsages() ? profile1.getPatternId() : profile2.getPatternId();
        AggregatedSpeedProfile aggregateProfile = new AggregatedSpeedProfile(aggregatedPatternId, profile1.getSamplingId());
        for (int i = 0 ; i < speedPerTime1.size(); i++)
        {
            int speed1 = speedPerTime1.get(i);
            int speed2 = speedPerTime2.get(i);
            int min1 = profile1.getMinAggregatedSpeedAt(i);
            int min2 = profile2.getMinAggregatedSpeedAt(i);
            int max1 = profile1.getMinAggregatedSpeedAt(i);
            int max2 = profile1.getMinAggregatedSpeedAt(i);

            if (!profile1.isNightTime(i) &&
                    (Math.abs(speed1 - speed2) > threshold
                            || Math.abs(min1 - min2) > rangeThreshold
                            || Math.abs(max1 - max2) > rangeThreshold
                    ))
            {
                return Optional.empty();
            }
            int aggregatedSpeed = speed1 == speed2 ? speed1 : getAverageSpeedByUsages(profile1, profile2, i);
            aggregateProfile.addSpeed(aggregatedSpeed);
            Pair<Integer, Integer> range = Pair.of(Math.min(min1, min2), Math.max(max1, max2));
            aggregateProfile.addRange(range);
        }
        aggregateProfile.addPatternId(profile1.getAggregatedPatternIDs())
                .addPatternId(profile2.getAggregatedPatternIDs())
                .addUsages(profile1.getUsages() + profile2.getUsages());
        return Optional.of(aggregateProfile);
    }

    @Override
    public void setThreshold(int value) {
        this.threshold = value;
        this.rangeThreshold = value * 2;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    private int getAverageSpeedByUsages(SpeedProfile profile1, SpeedProfile profile2, int timeIndex)
    {
        int usages1 = profile1.getUsages();
        int usages2 = profile2.getUsages();
        double sumUsages = (double) usages1 + usages2;
        double weight1 = usages1 / sumUsages;
        double weight2 = usages2 / sumUsages;
        return (int) Math.round(weight1 * profile1.getSpeedPerTime().get(timeIndex) + weight2 * profile2.getSpeedPerTime().get(timeIndex));
    }
}
