-- SELECT *  FROM all_tables where owner = 'CDCA_DEU_G4_SA_SO_18144' and table_name like '%LINK%' and table_name like '%NODE%';
    with TMP_RESTRICTED_UTURN_LINKS as
    (
        select ln.LINK_ID, ln.REF_NODE_ID, ln.NONREF_NODE_ID
        from RDF_LINK ln, RDF_ADMIN_ATTRIBUTE a, RDF_ADMIN_HIERARCHY h
        where a.ADMIN_PLACE_ID <> h.COUNTRY_ID
            and exists ( select 1 from RDF_NAV_LINK nl where nl.LINK_ID = ln.LINK_ID ) and
          ( ln.LEFT_ADMIN_PLACE_ID = h.ADMIN_PLACE_ID or ln.RIGHT_ADMIN_PLACE_ID = h.ADMIN_PLACE_ID ) and
          a.ADMIN_WIDE_REGULATIONS = 1 and
          ( a.ADMIN_PLACE_ID = h.ORDER1_ID or a.ADMIN_PLACE_ID = h.ORDER2_ID or a.ADMIN_PLACE_ID = h.ORDER8_ID or a.ADMIN_PLACE_ID = h.BUILTUP_ID )
    ),
    CF_PSF_LOCAL_LINK_RESTR_MNR as
    (
        -- RDF_CONDITION_DIVIDER (RR, SR, RS)
        select rcd.NODE_ID, rcd.FROM_LINK_ID, rcd.TO_LINK_ID, null as NAV_STRAND_ID from RDF_CONDITION_DIVIDER rcd
        join RDF_NAV_LINK rnl on rnl.LINK_ID = rcd.FROM_LINK_ID or rnl.LINK_ID = rcd.TO_LINK_ID
        join RDF_LINK rl on rl.LINK_ID = rnl.LINK_ID
        where rnl.DIVIDER != 'N' and
        (
            rnl.DIVIDER_LEGAL = 'N' or  rnl.DIVIDER = 'A'
            or (rl.REF_NODE_ID = rcd.NODE_ID and rnl.DIVIDER = '1')
            or (rl.NONREF_NODE_ID = rcd.NODE_ID and rnl.DIVIDER = '2')
        )
        union
        -- STUB_NAV_STRAND_CONDITION (SS, RS, SR)
        select rcd.NODE_ID, rcd.FROM_LINK_ID, rcd.TO_LINK_ID, null as NAV_STRAND_ID from STUB_CONDITION_DIVIDER rcd
        join STUB_NAV_LINK rnl on rnl.LINK_ID = rcd.FROM_LINK_ID or rnl.LINK_ID = rcd.TO_LINK_ID
        join STUB_LINK rl on rl.LINK_ID = rnl.LINK_ID
        where rnl.DIVIDER != 'N' and
        (
            rnl.DIVIDER_LEGAL = 'N' or  rnl.DIVIDER = 'A'
            or (rl.REF_NODE_ID = rcd.NODE_ID and rnl.DIVIDER = '1')
            or (rl.NONREF_NODE_ID = rcd.NODE_ID and rnl.DIVIDER = '2')
        ) and rcd.NODE_ID in (select NODE_ID from SC_BORDER_NODE)
        union
        -- U-Turn (any local links)
        select NODE_ID, FROM_LINK_ID, TO_LINK_ID, null as NAV_STRAND_ID from
        (
            select fromLinks.REF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from RDF_NAV_LINK rnl
            join RDF_LINK fromLinks on fromLinks.LINK_ID = rnl.LINK_ID
            and rnl.TRAVEL_DIRECTION = 'B'
            and rnl.DIVIDER != 'N' and
            (
                rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '1')
            )
            union
            select toLinks.NONREF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from RDF_NAV_LINK rnl
            join RDF_LINK toLinks on toLinks.LINK_ID = rnl.LINK_ID
            and rnl.TRAVEL_DIRECTION = 'B'
            and rnl.DIVIDER != 'N' and
            (
                rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '2')
            )
        ) where not exists
             (
                -- global ADMIN_PLACE_ID - country level (e.g. ITA)
                select 1 from RDF_ADMIN_ATTRIBUTE ra
                join RDF_ADMIN_HIERARCHY h on h.ADMIN_PLACE_ID = ra.ADMIN_PLACE_ID
                where h.ADMIN_PLACE_ID = h.COUNTRY_ID
                )
                and FROM_LINK_ID not in (select LINK_ID from TMP_RESTRICTED_UTURN_LINKS)
                and TO_LINK_ID not in (select LINK_ID from TMP_RESTRICTED_UTURN_LINKS)
        union
        -- U-Turn (stubble links)
        select NODE_ID, FROM_LINK_ID, TO_LINK_ID, null as NAV_STRAND_ID from
        (
            select fromLinks.REF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from STUB_NAV_LINK rnl
            join STUB_LINK fromLinks on fromLinks.LINK_ID = rnl.LINK_ID
            and rnl.TRAVEL_DIRECTION = 'B'
            and rnl.DIVIDER != 'N' and
            (
                rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '1')
            )
            -- FIXME: current countries with global UTurn restrictions
            where rnl.ISO_COUNTRY_CODE not in ('ITA', 'ARG', 'PRY', 'IDN', 'MYS', 'SGP')
            union
            select toLinks.NONREF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from STUB_NAV_LINK rnl
            join STUB_LINK toLinks on toLinks.LINK_ID = rnl.LINK_ID
            and rnl.TRAVEL_DIRECTION = 'B'
            and rnl.DIVIDER != 'N' and
            (
                rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '2')
            )
            where rnl.ISO_COUNTRY_CODE not in ('ITA', 'ARG', 'PRY', 'IDN', 'MYS', 'SGP')
            -- TODO: wait till NDSTECH-5623 be done
        ) where NODE_ID in (select NODE_ID from SC_BORDER_NODE)
        union
        -- Restricted Driving Manoeuvre pair (CONDITION_TYPE = 7)
        select fns.NODE_ID, fns.LINK_ID as FROM_LINK_ID, tns.LINK_ID as TO_LINK_ID, fns.NAV_STRAND_ID
        from RDF_NAV_STRAND fns
        join RDF_NAV_STRAND tns on tns.NAV_STRAND_ID = fns.NAV_STRAND_ID and tns.NODE_ID is null
                where fns.NODE_ID is not null and fns.NAV_STRAND_ID in
                (
                    select rn.NAV_STRAND_ID
                    from RDF_CONDITION rc
                    join RDF_NAV_STRAND rn on rn.nav_strand_id = rc.NAV_STRAND_ID
                    where rc.condition_type in (4, 7)
                    group by rn.NAV_STRAND_ID
                    having count (*) = 2
                )
                union
                -- Restricted Driving Manoeuvre pair (CONDITION_TYPE = 7)
                select fns.NODE_ID, fns.LINK_ID as FROM_LINK_ID, tns.LINK_ID as TO_LINK_ID, fns.NAV_STRAND_ID
                from STUB_NAV_STRAND fns
                join STUB_NAV_STRAND tns on tns.NAV_STRAND_ID = fns.NAV_STRAND_ID and tns.NODE_ID is null
                where fns.NODE_ID is not null and fns.NAV_STRAND_ID in
                (
                    select rn.NAV_STRAND_ID
                    from STUB_CONDITION rc
                    join STUB_NAV_STRAND rn on rn.nav_strand_id = rc.NAV_STRAND_ID
                    where rc.condition_type in (4, 7)
                    group by rn.NAV_STRAND_ID
                    having count (*) = 2
                ) and fns.NODE_ID in (select NODE_ID from SC_BORDER_NODE)
    ),
    TMP_STUB_LOCAL_BNODE_LINKS as
    (
        select rl.LINK_ID from RDF_LINK rl
        join RDF_NAV_LINK rnl on rnl.LINK_ID = rl.LINK_ID
        join CF_PSF_LOCAL_LINK_RESTR_MNR crm on crm.NODE_ID = rl.REF_NODE_ID
        where rl.REF_NODE_ID in (select NODE_ID from SC_BORDER_NODE)
            and rl.LINK_ID in (select LINK_ID from STUB_LINK_LOCAL)
        union
        select rl.LINK_ID from RDF_LINK rl
        join RDF_NAV_LINK rnl on rnl.LINK_ID = rl.LINK_ID
        join CF_PSF_LOCAL_LINK_RESTR_MNR crm on crm.NODE_ID = rl.NONREF_NODE_ID
        where rl.NONREF_NODE_ID in (select NODE_ID from SC_BORDER_NODE)
            and rl.LINK_ID in (select LINK_ID from STUB_LINK_LOCAL)
    )
    select distinct sll.LINK_ID, sll.STUB_ID
    from STUB_LINK_LOCAL sll
    join RDF_LINK l on sll.LINK_ID = l.LINK_ID
    join RDF_NAV_LINK rnl on l.LINK_ID = rnl.LINK_ID
    where sll.LINK_ID in (select LINK_ID from TMP_STUB_LOCAL_BNODE_LINKS)
