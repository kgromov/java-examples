package oracle.speed_profiles;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AggregatedSpeedProfile extends SpeedProfile {
    private Set<Integer> aggregatedPatternIDs = new TreeSet<>();

    public AggregatedSpeedProfile(int patternId, int samplingId) {
        super(patternId, samplingId);
    }

    public AggregatedSpeedProfile(int patternId, int samplingId, List<Integer> speedPerTime) {
        super(patternId, samplingId, speedPerTime);
    }

    public void setAggregatedPatternIDs(Set<Integer> aggregatedPatternIDs) {
        this.aggregatedPatternIDs = aggregatedPatternIDs;
    }

    public AggregatedSpeedProfile addPatternId(int patternId)
    {
        aggregatedPatternIDs.add(patternId);
        return this;
    }

    public AggregatedSpeedProfile addPatternId(Collection<Integer> patternIDs)
    {
        aggregatedPatternIDs.addAll(patternIDs);
        return this;
    }

    @Override
    public Set<Integer> getAggregatedPatternIDs() {
        return aggregatedPatternIDs;
    }
}
