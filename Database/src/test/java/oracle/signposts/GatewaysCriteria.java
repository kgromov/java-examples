package oracle.signposts;

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
            };
}
