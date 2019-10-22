package oracle.speed_profiles;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

public class SpeedProfile {
    private static final int MINUTE_IN_DAY = 24 * 60;

    private final int patternId;
    private final int samplingId;
    private final List<Integer> speedPerTime;
    private IntSummaryStatistics statistics;

    public SpeedProfile(int patternId, int samplingId) {
        this.patternId = patternId;
        this.samplingId = samplingId;
        // project specific
        int periods = samplingId == 1 ? MINUTE_IN_DAY / 60 : MINUTE_IN_DAY / 15;
        this.speedPerTime = new ArrayList<>(periods);
        this.statistics = speedPerTime.stream().mapToInt(i -> i).summaryStatistics();
    }

    public SpeedProfile(int patternId, int samplingId, List<Integer> speedPerTime) {
        this.patternId = patternId;
        this.samplingId = samplingId;
        this.speedPerTime = speedPerTime;
        this.statistics = speedPerTime.stream().mapToInt(i -> i).summaryStatistics();
    }

    public SpeedProfile addSpeed(int speed) {
        speedPerTime.add(speed);
        statistics.accept(speed);
        return this;
    }

    public int getMinSpeed() {
        return statistics.getMin();
    }

    public int getMaxSpeed() {
        return statistics.getMax();
    }

    public int getPatternId() {
        return patternId;
    }

    public int getSamplingId() {
        return samplingId;
    }

    public List<Integer> getSpeedPerTime() {
        return speedPerTime;
    }

    public boolean isMergeable(SpeedProfile otherProfile, int threshold) {
        return this.samplingId == otherProfile.samplingId
                && Math.abs(this.getMinSpeed() - otherProfile.getMinSpeed()) <= threshold
                && Math.abs(this.getMaxSpeed() - otherProfile.getMaxSpeed()) <= threshold;
    }

    public SpeedProfile getCalibratedProfile(int depth)
    {
        int periods = speedPerTime.size() / depth;
        List<Integer> speeds = new ArrayList<>(periods);
        for (int i = 0; i< periods; i++)
        {
            int resSpeed  = (int) Math.round(IntStream.range(0, depth).summaryStatistics().getAverage());
            speeds.add(resSpeed);
        }
        return new SpeedProfile(patternId, samplingId * depth, speeds);
    }

}
