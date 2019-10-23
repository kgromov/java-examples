package oracle.speed_profiles;

import oracle.checker.consumers.SpeedProfilesCriteriaConsumer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    public int getAverageSpeed()
    {
        return (int) statistics.getAverage();
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
                && Math.abs(this.getAverageSpeed() - otherProfile.getAverageSpeed()) <= threshold
              /*  && Math.abs(this.getMinSpeed() - otherProfile.getMinSpeed()) <= threshold
                && Math.abs(this.getMaxSpeed() - otherProfile.getMaxSpeed()) <= threshold*/;
    }

    public Set<Integer> getAggregatedPatternIDs()
    {
        return Collections.emptySet();
    }

    public SpeedProfile getCalibratedProfile(int depth) {
        if (depth == 1)
        {
            return this;
        }
        int periods = speedPerTime.size() / depth;
        List<Integer> speeds = new ArrayList<>(periods);
        for (int i = 0; i < periods; i++) {
            int finalI = i;
            int resSpeed = (int) Math.round(IntStream.range(0, depth).boxed()
                    .mapToInt(k -> speedPerTime.get(finalI * depth + k))
                    .summaryStatistics().getAverage());
            speeds.add(resSpeed);
        }
//        return new SpeedProfile(patternId, samplingId * depth, speeds);
        return new SpeedProfile(patternId, 2, speeds);
    }

    public List<SpeedProfilesCriteriaConsumer.SpeedProfileRow> toDbRows() {
        int seqNums = speedPerTime.size();
        long deltaInMinutes = MINUTE_IN_DAY / seqNums;
        List<LocalTime> timePeriods = new ArrayList<>(seqNums);
        LocalTime midnight = LocalTime.MIDNIGHT;
        timePeriods.add(midnight);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 1; i < seqNums; i++)
        {
            LocalTime startTime = i == 1 ? midnight : timePeriods.get(i - 1);
            timePeriods.add(startTime.plusMinutes(deltaInMinutes));
        }
        return IntStream.range(0, seqNums).boxed()
                .map(i -> new SpeedProfilesCriteriaConsumer.SpeedProfileRow(
                        patternId,
                        i + 1,
                        samplingId,
                        timePeriods.get(i).format(formatter),
                        timePeriods.get((i + 1) % seqNums).format(formatter),
                        speedPerTime.get(i)
                ))
                .collect(Collectors.toList());
    }

}
