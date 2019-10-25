package oracle.speed_profiles.merge;

import oracle.speed_profiles.AggregatedSpeedProfile;
import oracle.speed_profiles.SpeedProfile;

import java.util.List;
import java.util.Optional;

public class NeighboursAverageMergeStrategy implements MergeStrategy {


    @Override
    public Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2) {
        if (!profile1.isMergeable(profile2, DEFAULT_THRESHOLD))
        {
            return Optional.empty();
        }
        List<Integer> speedPerTime1 = profile1.getSpeedPerTime();
        List<Integer> speedPerTime2 = profile2.getSpeedPerTime();

        AggregatedSpeedProfile aggregateProfile = new AggregatedSpeedProfile(profile1.getPatternId(), profile1.getSamplingId());
        aggregateProfile.addPatternId(profile1.getAggregatedPatternIDs());
        for (int i = 0 ; i < speedPerTime1.size() / 2 ; i++)
        {
            int startIndex = i * 2;
            int endIndex = i * 2 + 1;
            int avgSpeed1 = Math.round((speedPerTime1.get(startIndex) + speedPerTime1.get(endIndex)) / DIV);
            int avgSpeed2 = Math.round((speedPerTime2.get(startIndex) + speedPerTime2.get(endIndex)) / DIV);
            if (Math.abs(avgSpeed1 - avgSpeed2) >  DEFAULT_THRESHOLD)
            {
                return Optional.empty();
            }
            int aggregatedSpeed = Math.round((avgSpeed1 + avgSpeed2) / DIV);
//            int aggregatedSpeed = MergeStrategy.roundToThreshold(avgSpeed1, avgSpeed2);
            aggregateProfile.addSpeed(aggregatedSpeed);
            aggregateProfile.addSpeed(aggregatedSpeed);
        }
        aggregateProfile.addPatternId(profile1.getPatternId());
        aggregateProfile.addPatternId(profile2.getPatternId());
        return Optional.of(aggregateProfile);
    }
}
