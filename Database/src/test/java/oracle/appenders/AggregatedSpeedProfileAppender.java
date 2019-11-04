package oracle.appenders;

import com.google.common.collect.ImmutableMap;
import oracle.speed_profiles.SpeedProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class AggregatedSpeedProfileAppender implements Appender<SpeedProfile> {
    private static final String MERGED_PATTERN_IDS_TEMPLATE = "%d :%s";
    private static final String TABLE_NAME = "NTTP_SPEED_PATTERN_AGGREGATION";
    private static final String TEXT_TYPE = "TEXT";
    private static final String INTEGER_TYPE = "INTEGER";

    private static final Map<String, String> COLUMN_TYPE_BY_COLUMN_NAME = ImmutableMap.<String, String>builder()
            .put("PATTERN_ID", INTEGER_TYPE)
            .put("MERGED_PATTERN_IDS", TEXT_TYPE)
            .put("TOTAL_USAGES", INTEGER_TYPE)
            .build();

    @Override
    public void append(PreparedStatement statement, SpeedProfile object) {

    }

    @Override
    public void append(Connection connection, String query, SpeedProfile object) {

    }


    public void append(Connection connection, Collection<SpeedProfile> speedProfiles) throws SQLException {
        String query = getInsertQuery(TABLE_NAME, COLUMN_TYPE_BY_COLUMN_NAME);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            for (SpeedProfile row : speedProfiles) {
                int columnIndex = 0;
                statement.setInt(++columnIndex, row.getPatternId());
                statement.setString(++columnIndex, String.format(MERGED_PATTERN_IDS_TEMPLATE,
                        row.getAggregatedPatternIDs().size(), row.getAggregatedPatternIDs()));
                statement.setInt(++columnIndex, row.getPatternId());
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        }
    }
}
