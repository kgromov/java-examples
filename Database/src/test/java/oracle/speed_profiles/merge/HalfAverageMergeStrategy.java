package oracle.speed_profiles.merge;

import oracle.speed_profiles.AggregatedSpeedProfile;
import oracle.speed_profiles.SpeedProfile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HalfAverageMergeStrategy implements MergeStrategy {
    private Map<Integer, Integer> profilesUsage;

    public HalfAverageMergeStrategy(Map<Integer, Integer> profilesUsage) {
        this.profilesUsage = profilesUsage;
    }

    @Override
    public Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2)
    {
        if (!profile1.isMergeable(profile2, DEFAULT_THRESHOLD))
        {
            return Optional.empty();
        }
        List<Integer> speedPerTime1 = profile1.getSpeedPerTime();
        List<Integer> speedPerTime2 = profile2.getSpeedPerTime();

        int aggregatedPatternId = getUsages(profile1) >=getUsages(profile2) ? profile1.getPatternId() : profile2.getPatternId();
        AggregatedSpeedProfile aggregateProfile = new AggregatedSpeedProfile(aggregatedPatternId, profile1.getSamplingId());
        for (int i = 0 ; i < speedPerTime1.size(); i++)
        {
            int speed1 = speedPerTime1.get(i);
            int speed2 = speedPerTime2.get(i);
//            int threshold = profile1.isNightTime(i) ? DEFAULT_THRESHOLD * 2 : DEFAULT_THRESHOLD;
            if (profile1.isNightTime(i) && Math.abs(speed1 - speed2) >  DEFAULT_THRESHOLD)
//            if ( Math.abs(speed1 - speed2) >  threshold)
            {
                return Optional.empty();
            }
//            int aggregatedSpeed = Math.round((speed1 + speed2) / DIV);
//            int aggregatedSpeed = MergeStrategy.roundToThreshold(speed1, speed2);
            int aggregatedSpeed = speed1 == speed2 ? speed1 : getAverageSpeedByUsages(profile1, profile2, i);
            aggregateProfile.addSpeed(aggregatedSpeed);
        }
        aggregateProfile.addPatternId(profile1.getAggregatedPatternIDs());
        aggregateProfile.addPatternId(profile2.getAggregatedPatternIDs());
        return Optional.of(aggregateProfile);
    }

    private int getUsages(SpeedProfile profile)
    {
        return profilesUsage.getOrDefault(profile.getPatternId(), 0);
    }

    private int getAverageSpeedByUsages(SpeedProfile profile1, SpeedProfile profile2, int timeIndex)
    {
        int usages1 = getUsages(profile1);
        int usages2 = getUsages(profile2);
        double sumUsages = (double) usages1 + usages2;
        double weight1 = usages1 / sumUsages;
        double weight2 = usages2 / sumUsages;
        return (int) Math.round(weight1 * profile1.getSpeedPerTime().get(timeIndex) + weight2 * profile2.getSpeedPerTime().get(timeIndex));
    }
}
