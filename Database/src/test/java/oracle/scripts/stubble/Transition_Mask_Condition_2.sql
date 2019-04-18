with CF_PSF_STUB_LINK_RESTR_MNR as
(
                 -- RDF_CONDITION_DIVIDER
                select rcd.NODE_ID, rcd.FROM_LINK_ID, rcd.TO_LINK_ID from RDF_CONDITION_DIVIDER rcd
--                join TMP_CF_PSF_RDF_NAV_LINK rnl on rnl.LINK_ID = rcd.FROM_LINK_ID or rnl.LINK_ID = rcd.TO_LINK_ID
                join RDF_NAV_LINK rnl on rnl.LINK_ID = rcd.FROM_LINK_ID or rnl.LINK_ID = rcd.TO_LINK_ID
                join RDF_LINK rl on rl.LINK_ID = rnl.LINK_ID
                where rnl.DIVIDER != 'N' and
                (
                    rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER = 'A'
                    or (rl.REF_NODE_ID = rcd.NODE_ID and rnl.DIVIDER = '1')
                    or (rl.NONREF_NODE_ID = rcd.NODE_ID and rnl.DIVIDER = '2')
                )
                union
                -- U-Turn
                select NODE_ID, FROM_LINK_ID, TO_LINK_ID from
                (
                    select fromLinks.REF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from STUB_NAV_LINK rnl
--                    inner join TMP_STUB_NAV_LINK_INF snli on rnl.LINK_ID = snli.LINK_ID
                    join STUB_LINK fromLinks on fromLinks.LINK_ID = rnl.LINK_ID
                    and rnl.TRAVEL_DIRECTION = 'B'
                    and rnl.DIVIDER != 'N' and
                    (
                        rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '1')
                    )
                    union
                    select toLinks.NONREF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from STUB_NAV_LINK rnl
--                    inner join TMP_STUB_NAV_LINK_INF snli on rnl.LINK_ID = snli.LINK_ID
                    join STUB_LINK toLinks on toLinks.LINK_ID = rnl.LINK_ID
                    and rnl.TRAVEL_DIRECTION = 'B'
                    and rnl.DIVIDER != 'N' and
                    (
                        rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '2')
                    )
                ) where not exists (select 1 from RDF_ADMIN_ATTRIBUTE where ADMIN_WIDE_REGULATIONS = 1)
                union
                -- U-Turn (stubble links)
                select NODE_ID, FROM_LINK_ID, TO_LINK_ID from
                (
                    select fromLinks.REF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from STUB_NAV_LINK rnl
--                    inner join TMP_STUB_NAV_LINK_INF snli on rnl.LINK_ID = snli.LINK_ID
                    join STUB_LINK fromLinks on fromLinks.LINK_ID = rnl.LINK_ID
                    and rnl.TRAVEL_DIRECTION = 'B'
                    and rnl.DIVIDER != 'N' and
                    (
                        rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '1')
                    )
                    union
                    select toLinks.NONREF_NODE_ID as NODE_ID, rnl.LINK_ID as FROM_LINK_ID, rnl.LINK_ID as TO_LINK_ID from STUB_NAV_LINK rnl
--                    inner join TMP_STUB_NAV_LINK_INF snli on rnl.LINK_ID = snli.LINK_ID
                    join STUB_LINK toLinks on toLinks.LINK_ID = rnl.LINK_ID
                    and rnl.TRAVEL_DIRECTION = 'B'
                    and rnl.DIVIDER != 'N' and
                    (
                        rnl.DIVIDER_LEGAL = 'N' or rnl.DIVIDER in ('A', '2')
                    )
                ) where not exists (select 1 from RDF_ADMIN_ATTRIBUTE where ADMIN_WIDE_REGULATIONS = 1)
                    and NODE_ID in (select NODE_ID from SC_BORDER_NODE)
            ),
    TMP_STUB_BNODE_LINKS as
    (
        select rl.LINK_ID from STUB_LINK rl
        join STUB_NAV_LINK rnl on rnl.LINK_ID = rl.LINK_ID
        join CF_PSF_STUB_LINK_RESTR_MNR crm on crm.NODE_ID = rl.REF_NODE_ID
        where rl.REF_NODE_ID in (select NODE_ID from SC_BORDER_NODE)
            and rl.LINK_ID in (select LINK_ID from STUB_NAV_LINK)
        union
        select rl.LINK_ID from STUB_LINK rl
        join STUB_NAV_LINK rnl on rnl.LINK_ID = rl.LINK_ID
        join CF_PSF_STUB_LINK_RESTR_MNR crm on crm.NODE_ID = rl.NONREF_NODE_ID
        where rl.NONREF_NODE_ID in (select NODE_ID from SC_BORDER_NODE)
            and rl.LINK_ID in (select LINK_ID from STUB_NAV_LINK)
    )
    select distinct snl.LINK_ID, sl.STUB_ID
    from STUB_NAV_LINK snl
    join STUB_LINK sl on snl.LINK_ID = sl.LINK_ID
    where snl.LINK_ID in (select LINK_ID from TMP_STUB_BNODE_LINKS)
        -- Part of TRANSITION_MASK
--       exists (
--            select 1
--            from RDF_CONDITION_DIVIDER cd
--            where (snl.LINK_ID = cd.TO_LINK_ID or snl.LINK_ID = cd.FROM_LINK_ID)
--                and cd.NODE_ID in (select NODE_ID from SC_BORDER_NODE)
--        )
--         exists (
--            select 1
--            from CF_PSF_STUB_LINK_RESTR_MNR crm
--            where (snl.LINK_ID = crm.TO_LINK_ID or snl.LINK_ID = crm.FROM_LINK_ID)
--                and crm.NODE_ID in (select NODE_ID from SC_BORDER_NODE)
--        )
