package oracle.speed_profiles;

import java.util.List;
import java.util.Optional;

public class HalfAverageMerge implements Mergeable {
    private static final int DEFAULT_THRESHOLD = 5;
    private static final float DIV = 2;

    @Override
    public Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2, int depth)
    {
        SpeedProfile updatedProfile1 = profile1.getCalibratedProfile(depth);
        SpeedProfile updatedProfile2 = profile2.getCalibratedProfile(depth);
        if (updatedProfile1.isMergeable(updatedProfile2, DEFAULT_THRESHOLD))
        {
            return Optional.empty();
        }
        List<Integer> speedPerTime1 = updatedProfile1.getSpeedPerTime();
        List<Integer> speedPerTime2 = updatedProfile2.getSpeedPerTime();

        AggregatedSpeedProfile aggregateProfile = new AggregatedSpeedProfile(updatedProfile1.getPatternId(), updatedProfile1.getSamplingId());
        for (int i = 0 ; i < speedPerTime1.size(); i++)
        {
            int avgSpeed1 = Math.round((speedPerTime1.get(i * 2) + speedPerTime1.get(i* 2 + 1)) / DIV);
            int avgSpeed2 = Math.round((speedPerTime2.get(i * 2) + speedPerTime2.get(i* 2 + 1)) / DIV);
            if (Math.abs(avgSpeed1 - avgSpeed2) >  DEFAULT_THRESHOLD)
            {
                return Optional.empty();

            }
            int div = Math.max(Math.round(avgSpeed1 / DEFAULT_THRESHOLD), avgSpeed2 / DEFAULT_THRESHOLD);
            int aggregatedSpeed = div * DEFAULT_THRESHOLD;
            aggregateProfile.addPatternId(aggregatedSpeed);
        }
        aggregateProfile.addPatternId(profile1.getPatternId());
        aggregateProfile.addPatternId(profile2.getPatternId());
        return Optional.of(aggregateProfile);
    }
}
