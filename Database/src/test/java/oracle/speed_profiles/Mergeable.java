package oracle.speed_profiles;

import java.util.Optional;

public interface Mergeable {
    Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2, int depth);
}
