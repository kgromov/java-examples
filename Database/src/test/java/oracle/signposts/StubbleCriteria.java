package oracle.signposts;

import oracle.signposts.criterias.ICriteria;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum StubbleCriteria implements ICriteria {
    STUB_POI
       {
            @Override
            public String getQuery() {
                return "select distinct snl.LINK_ID\n" +
                        "from STUB_NAV_LINK snl\n" +
                        "join RDF_LOCATION l on snl.LINK_ID = l.LINK_ID\n" +
                        "join RDF_POI_ADDRESS rpa on rpa.LOCATION_ID = l.LOCATION_ID\n" +
                        "join RDF_POI rp on rp.POI_ID = rpa.POI_ID\n" +
                        "where snl.POI_ACCESS = 'Y'";
        }
    },

    STUB_LOCAL_POI
            {
                @Override
                public String getQuery() {
                    return "select distinct rnl.LINK_ID\n" +
                            "from RDF_NAV_LINK rnl\n" +
                            "join STUB_LINK_LOCAL sll on sll.LINK_ID = rnl.LINK_ID\n" +
                            "join RDF_LOCATION l on rnl.LINK_ID = l.LINK_ID\n" +
                            "join RDF_POI_ADDRESS rpa on rpa.LOCATION_ID = l.LOCATION_ID\n" +
                            "join RDF_POI rp on rp.POI_ID = rpa.POI_ID\n" +
                            "where rnl.POI_ACCESS = 'Y'";
                }
            };

    @Override
    public String getIdentity(ResultSet resultSet) throws SQLException {
        StringBuilder builder = new StringBuilder(this.name());
        builder.append(':').append('\t').append(resultSet.getInt("LINK_ID"));
        while(resultSet.next())
        {
            builder.append(", ").append(resultSet.getInt("LINK_ID"));
        }
        return builder.toString();
    }
}
