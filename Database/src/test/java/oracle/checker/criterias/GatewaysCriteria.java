package oracle.checker.criterias;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum GatewaysCriteria implements ICriteria {
    CONTINUE_TURN_RESTRICTION
            {
                @Override
                public String getQuery() {
                    return "with STUB_NAV_STRAND_CONDITION as " +
                            "(" +
                            "    select c.CONDITION_ID, c.CONDITION_TYPE, c.ACCESS_ID, ns.NAV_STRAND_ID, ns.SEQ_NUM, ns.LINK_ID, ns.NODE_ID from STUB_CONDITION c " +
                            "    join STUB_NAV_STRAND ns on c.NAV_STRAND_ID = ns.NAV_STRAND_ID " +
                            "    where bitand(c.ACCESS_ID, 512) > 0 and c.CONDITION_TYPE in (4, 7) " +
                            ")" +
                            "select NAV_STRAND_ID from STUB_NAV_STRAND_CONDITION " +
                            "group by NAV_STRAND_ID " +
                            "having count(*) > 2";
                }
            },
    ADMIN_WIDE_REGULATIONS
            {
                @Override
                public String getIdentity(ResultSet resultSet) throws SQLException {
                    return String.format("LINKS_WITH_RESTRICTIONS = %d, TOTAL_LINKS = %d",
                            resultSet.getInt(1), resultSet.getInt(2));
                }

                @Override
                public String getQuery() {
                    return "select COALESCE( h.LINKS_WITH_RESTRICTIONS, l.TOTAL_LINKS) as LINKS_WITH_RESTRICTIONS, l.TOTAL_LINKS " +
                            "from " +
                            "(" +
                            "    select sum(num_links) as LINKS_WITH_RESTRICTIONS from RDF_ADMIN_HIERARCHY" +
                            "    where admin_place_id in (select ADMIN_PLACE_ID from RDF_ADMIN_ATTRIBUTE)" +
                            ") h," +
                            "(" +
                            "    select count(LINK_ID) as TOTAL_LINKS from rdf_nav_link" +
                            ") l " +
                            "where exists (select 1 from RDF_ADMIN_ATTRIBUTE)";
                }
            },
    REGIONS_WITH_UTURN_RESTRICTIONS
            {
                @Override
                public String getIdentity(ResultSet resultSet) throws SQLException {
                    return String.format("ADMIN_PLACE_ID = %d, ADMIN_WIDE_REGULATIONS = %s",
                            resultSet.getInt(1), resultSet.getObject(2));
                }

                @Override
                public String getQuery() {
                    return "select ADMIN_PLACE_ID, ADMIN_WIDE_REGULATIONS from RDF_ADMIN_ATTRIBUTE";
                }
            }
}
