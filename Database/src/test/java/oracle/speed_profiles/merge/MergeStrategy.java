package oracle.speed_profiles.merge;

import oracle.speed_profiles.SpeedProfile;

import java.util.Optional;

public interface MergeStrategy {
    int DEFAULT_THRESHOLD = 5;
    float DIV = 2;

    Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2);

    static int roundToThreshold(int value1, int value2)
    {
        int div = Math.max(Math.round(value1 / DEFAULT_THRESHOLD), value2 / DEFAULT_THRESHOLD);
        return div * DEFAULT_THRESHOLD;
    }
}
