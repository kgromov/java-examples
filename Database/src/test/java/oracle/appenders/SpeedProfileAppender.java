package oracle.appenders;

import com.google.common.collect.ImmutableMap;
import oracle.speed_profiles.SpeedProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Map;

public class SpeedProfileAppender implements Appender<SpeedProfile> {
    public static final String TABLE_NAME = "NTTP_SPEED_PATTERN";
    private static final String TEXT_TYPE = "TEXT";
    private static final String INTEGER_TYPE = "INTEGER";

    private static final Map<String, String> COLUMN_TYPE_BY_COLUMN_NAME = ImmutableMap.<String, String>builder()
            .put("PATTERN_ID", INTEGER_TYPE)
            .put("SEQ_NUM", INTEGER_TYPE)
            .put("SAMPLING_ID", INTEGER_TYPE)
            .put("START_TIME", TEXT_TYPE)
            .put("END_TIME", TEXT_TYPE)
            .put("SPEED_KPH", INTEGER_TYPE)
            .build();

    @Override
    public void append(PreparedStatement statement, SpeedProfile object) {

    }

    @Override
    public void append(Connection connection, String query, SpeedProfile object) {

    }

    @Override
    public void append(Connection connection, Collection<SpeedProfile> objects) {

    }
}
