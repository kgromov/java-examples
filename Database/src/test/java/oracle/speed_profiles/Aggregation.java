package oracle.speed_profiles;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Aggregation {
    private int depth;
    private List<SpeedProfile> speedProfiles;

    public Aggregation(int depth) {
        this.depth = depth;
    }

    public Aggregation(List<SpeedProfile> speedProfiles) {
        this.speedProfiles = speedProfiles;
    }

    public List<? extends SpeedProfile> aggregateProfiles(List<? extends SpeedProfile> speedProfiles, int iterations)
    {
        HalfAverageMerge merge = new HalfAverageMerge();
        List<SpeedProfile> aggregatedSpeedProfiles = new ArrayList<>();
        for (int i = 0; i < speedProfiles.size() / 2; i++)
        {
            merge.getAggregatedProfile(speedProfiles.get(i * 2), speedProfiles.get( i * 2 +1), depth)
                    .ifPresent(aggregatedSpeedProfiles::add);
        }
        ++iterations;
        return  aggregatedSpeedProfiles.size() <= 200 || iterations > 20
                ? aggregatedSpeedProfiles
                : aggregateProfiles(aggregatedSpeedProfiles, iterations);
    }

    public static void main(String[] args) {
        Set<String> markets = Sets.newHashSet("eu", "nar", "mrm");
        try {
            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
