package oracle.speed_profiles;

import java.util.List;
import java.util.Optional;

public class HalfAverageMerge implements Mergeable {
    private static final int DEFAULT_THRESHOLD = 5;
    private static final float DIV = 2;

    @Override
    public Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2)
    {
        int depth1 = Aggregation.AGGREGATION_DEPTH.getOrDefault(profile1.getSamplingId(), 1);
        int depth2 = Aggregation.AGGREGATION_DEPTH.getOrDefault(profile2.getSamplingId(), 1);
        SpeedProfile updatedProfile1 = profile1.getCalibratedProfile(depth1);
        SpeedProfile updatedProfile2 = profile2.getCalibratedProfile(depth2);
        if (!updatedProfile1.isMergeable(updatedProfile2, DEFAULT_THRESHOLD))
        {
            return Optional.empty();
        }
        List<Integer> speedPerTime1 = updatedProfile1.getSpeedPerTime();
        List<Integer> speedPerTime2 = updatedProfile2.getSpeedPerTime();

        AggregatedSpeedProfile aggregateProfile = new AggregatedSpeedProfile(updatedProfile1.getPatternId(), updatedProfile1.getSamplingId());
        aggregateProfile.addPatternId(updatedProfile1.getAggregatedPatternIDs());
        for (int i = 0 ; i < speedPerTime1.size(); i++)
        {
            int endIndex = i == speedPerTime1.size() - 1 ? i - 1 : i + 1;
            int avgSpeed1 = Math.round((speedPerTime1.get(i) + speedPerTime1.get(endIndex)) / DIV);
            int avgSpeed2 = Math.round((speedPerTime2.get(i) + speedPerTime2.get(endIndex)) / DIV);
            if (Math.abs(avgSpeed1 - avgSpeed2) >  DEFAULT_THRESHOLD)
            {
                return Optional.empty();
            }
            int div = Math.max(Math.round(avgSpeed1 / DEFAULT_THRESHOLD), avgSpeed2 / DEFAULT_THRESHOLD);
            int aggregatedSpeed = div * DEFAULT_THRESHOLD;


         /*   int avgSpeed1 = speedPerTime1.get(i);
            int avgSpeed2 = speedPerTime2.get(i);
           *//* if (Math.abs(avgSpeed1 - avgSpeed2) >  DEFAULT_THRESHOLD)
            {
                return Optional.empty();
            }*//*
            int aggregatedSpeed = Math.round((avgSpeed1 + avgSpeed2) / DIV)*/;
            aggregateProfile.addSpeed(aggregatedSpeed);
        }
        aggregateProfile.addPatternId(profile1.getPatternId());
        aggregateProfile.addPatternId(profile2.getPatternId());
        return Optional.of(aggregateProfile);
    }
}
