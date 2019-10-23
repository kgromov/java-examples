package oracle.speed_profiles;

import java.util.Optional;
import java.util.concurrent.RecursiveTask;

public class FJPoolMerge extends RecursiveTask<SpeedProfile> implements Mergeable {
    @Override
    protected SpeedProfile compute() {
        return null;
    }

    @Override
    public Optional<? extends SpeedProfile> getAggregatedProfile(SpeedProfile profile1, SpeedProfile profile2) {
        return Optional.empty();
    }
}
