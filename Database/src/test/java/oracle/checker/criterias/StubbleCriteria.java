package oracle.checker.criterias;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum StubbleCriteria implements ICriteria {
    STUB_POI
       {

           @Override
            public String getQuery() {
                return "select distinct snl.LINK_ID, snl.POI_ACCESS, rp.POI_ID, rp.CAT_ID\n" +
                        "from STUB_NAV_LINK snl\n" +
                        "join RDF_LOCATION rl on rl.LINK_ID = snl.LINK_ID\n" +
                        "join RDF_POI_ADDRESS rpa on rpa.LOCATION_ID = rl.LOCATION_ID\n" +
                        "join RDF_POI rp on rp.POI_ID = rpa.POI_ID";
        }

           @Override
           public String getIdentity(ResultSet resultSet) throws SQLException {
               return String.format("STUB_POI: {LINK_ID = %d, POI_ACCESS = '%s' POI_ID = %d, CAT_ID = %d}",
                       resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4));
           }
       },

    STUB_LOCAL_POI
            {
                @Override
                public String getQuery() {
                    return "select distinct snl.LINK_ID, rnl.POI_ACCESS, rp.POI_ID, rp.CAT_ID\n" +
                            "from STUB_LINK_LOCAL snl\n" +
                            "join RDF_NAV_LINK rnl on rnl.LINK_ID = snl.LINK_ID\n" +
                            "join RDF_LOCATION rl on rl.LINK_ID = snl.LINK_ID\n" +
                            "join RDF_POI_ADDRESS rpa on rpa.LOCATION_ID = rl.LOCATION_ID\n" +
                            "join RDF_POI rp on rp.POI_ID = rpa.POI_ID";
                }

                @Override
                public String getIdentity(ResultSet resultSet) throws SQLException {
                    return String.format("STUB_LOCAL_POI: {LINK_ID = %d, POI_ACCESS = '%s' POI_ID = %d, CAT_ID = %d}",
                            resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4));
                }
            },
    STUB_POI_AIRPORT
            {

                @Override
                public String getQuery() {
                    return "select distinct l.LINK_ID\n" +
                            "from RDF_LOCATION l\n" +
                            "join STUB_LINK sl on l.LINK_ID = sl.LINK_ID\n" +
                            "join RDF_POI_ADDRESS pa on pa.LOCATION_ID = l.LOCATION_ID\n" +
                            "join RDF_POI p on p.POI_ID = pa.POI_ID\n" +
                            "join RDF_POI_AIRPORT rpa on rpa.POI_ID = pa.POI_ID";
                }
            },

    STUB_LOCAL_POI_AIRPORT
            {
                @Override
                public String getQuery() {
                    return "select distinct l.LINK_ID\n" +
                            "from RDF_LOCATION l\n" +
                            "join STUB_LINK_LOCAL sl on l.LINK_ID = sl.LINK_ID\n" +
                            "join RDF_POI_ADDRESS pa on pa.LOCATION_ID = l.LOCATION_ID\n" +
                            "join RDF_POI p on p.POI_ID = pa.POI_ID\n" +
                            "join RDF_POI_AIRPORT rpa on rpa.POI_ID = pa.POI_ID";
                }
            },
    STUB_POI_COUNT_DIFF
    {
        @Override
        public String getQuery() {
            return null;
        }

        @Override
        public String getQuery(String schema) {
            return new StringBuilder("select no_poi_id,  with_poi_id\n")
                    .append("from (\n")
                    .append("select COUNT(LINK_ID) as no_poi_id from (\n")
                    // without RDF_POI_ADDRESS
                    .append(STUB_POI.getQuery(schema))
                    .append(")\n),\n")
                    .append("(\nselect COUNT(LINK_ID) as with_poi_id from (\n")
                    // with RDF_POI_ADDRESS
                    .append("select distinct snl.LINK_ID\n")
                    .append("from ").append(schema).append('.').append("STUB_NAV_LINK snl\n")
                    .append("join ").append(schema).append('.').append("RDF_LOCATION l on snl.LINK_ID = l.LINK_ID\n")
                    .append("join ").append(schema).append('.').append("RDF_POI_ADDRESS rpa on rpa.LOCATION_ID = l.LOCATION_ID\n")
                    .append("join ").append(schema).append('.').append("RDF_POI rp on rp.POI_ID = rpa.POI_ID\n")
                    .append("where snl.POI_ACCESS = 'Y'")
                    .append(")\n)\n")
                    .append("where no_poi_id <> with_poi_id")
                    .toString();
        }

        @Override
        public String getIdentity(ResultSet resultSet) throws SQLException {
            return String.format("STUB_POI_COUNT_DIFF: no_poi_id = %d, with_poi_id = %d",
                    resultSet.getInt(1), resultSet.getInt(2));
        }
    },
    STUB_LOCAL_POI_COUNT_DIFF {
        @Override
        public String getQuery() {
            return null;
        }

        @Override
        public String getQuery(String schema) {
            return new StringBuilder("select no_poi_id,  with_poi_id\n")
                    .append("from (\n")
                    .append("select COUNT(LINK_ID) as no_poi_id from (\n")
                    // without RDF_POI_ADDRESS
                    .append(STUB_LOCAL_POI.getQuery(schema))
                    .append(")\n),\n")
                    .append("(\nselect COUNT(LINK_ID) as with_poi_id from (\n")
                    // with RDF_POI_ADDRESS
                    .append("select distinct snl.LINK_ID\n")
                    .append("from ").append(schema).append('.').append("RDF_NAV_LINK snl\n")
                    .append("join ").append(schema).append('.').append("STUB_LINK_LOCAL sll on sll.LINK_ID = snl.LINK_ID\n")
                    .append("join ").append(schema).append('.').append("RDF_LOCATION l on snl.LINK_ID = l.LINK_ID\n")
                    .append("join ").append(schema).append('.').append("RDF_POI_ADDRESS rpa on rpa.LOCATION_ID = l.LOCATION_ID\n")
                    .append("join ").append(schema).append('.').append("RDF_POI rp on rp.POI_ID = rpa.POI_ID\n")
                    .append("where snl.POI_ACCESS = 'Y'")
                    .append(")\n)\n")
                    .append("where no_poi_id <> with_poi_id")
                    .toString();
        }

        @Override
        public String getIdentity(ResultSet resultSet) throws SQLException {
            return String.format("STUB_LOCAL_POI_COUNT_DIFF: no_poi_id = %d, with_poi_id = %d",
                    resultSet.getInt(1), resultSet.getInt(2));
        }
    };

/*    @Override
    public String getIdentity(ResultSet resultSet) throws SQLException {
        StringBuilder builder = new StringBuilder(this.name());
        builder.append(':').append('\t').append(resultSet.getInt("LINK_ID"));
        while(resultSet.next())
        {
            builder.append(", ").append(resultSet.getInt("LINK_ID"));
        }
        return builder.toString();
    }*/
}
