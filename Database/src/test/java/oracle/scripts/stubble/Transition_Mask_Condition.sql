with CF_REAL_LINK_INTERN_RESTR as
(
    select distinct ns_first.NAV_STRAND_ID
                    from RDF_NAV_STRAND ns_first
                    join RDF_NAV_STRAND ns_last on ns_first.NAV_STRAND_ID = ns_last.NAV_STRAND_ID
                    join RDF_CONDITION c on ns_first.NAV_STRAND_ID = c.NAV_STRAND_ID
                    join RDF_NAV_LINK nl on (nl.LINK_ID = ns_first.LINK_ID or nl.LINK_ID = ns_last.LINK_ID)
                    where c.CONDITION_TYPE = 7 and ns_first.SEQ_NUM = 0 and ns_last.SEQ_NUM in
                    (
                        select max(seq_num)
                        from RDF_NAV_STRAND ns_all
                        where ns_all.NAV_STRAND_ID = ns_first.NAV_STRAND_ID
                    )
),
CF_PSF_STUB_LINK_RESTR_MNR as
(
                select rcd.NODE_ID, rcd.FROM_LINK_ID, rcd.TO_LINK_ID from STUB_NAV_LINK rnl
--                inner join TMP_STUB_NAV_LINK_INF snli on rnl.LINK_ID = snli.LINK_ID
                join STUB_LINK fromLinks on fromLinks.LINK_ID = rnl.LINK_ID
                and rnl.DIVIDER != 'N' and
                (
                    rnl.DIVIDER_LEGAL = 'N' or (rnl.DIVIDER != 'A' or
                    (fromLinks.LINK_ID = rnl.LINK_ID and rnl.DIVIDER = '1'))
                )
                join STUB_LINK toLinks on  toLinks.LINK_ID = rnl.LINK_ID
                and rnl.DIVIDER != 'N' and
                (
                    rnl.DIVIDER_LEGAL = 'N' or (rnl.DIVIDER != 'A' or
                    (toLinks.LINK_ID = rnl.LINK_ID and rnl.DIVIDER = '2'))
                )
                join RDF_CONDITION_DIVIDER rcd on  fromLinks.LINK_ID = rcd.FROM_LINK_ID or toLinks.LINK_ID = rcd.TO_LINK_ID
                where (fromLinks.REF_NODE_ID = rcd.NODE_ID or fromLinks.NONREF_NODE_ID = rcd.NODE_ID)
                and (toLinks.REF_NODE_ID = rcd.NODE_ID or toLinks.NONREF_NODE_ID = rcd.NODE_ID)
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
                -- Restricted Driving Manoeuvre (RDF_CONDITION = 7)
                select fns.NODE_ID, fns.LINK_ID as FROM_LINK_ID, tns.LINK_ID as TO_LINK_ID
                from RDF_NAV_STRAND fns
                join CF_REAL_LINK_INTERN_RESTR lir on lir.NAV_STRAND_ID = fns.NAV_STRAND_ID
                join RDF_NAV_STRAND tns on tns.NAV_STRAND_ID = fns.NAV_STRAND_ID and tns.NODE_ID is null
                where fns.NODE_ID is not null and fns.NAV_STRAND_ID in
                (
                    select rn.NAV_STRAND_ID
                    from rdf_condition rc
                    join RDF_NAV_STRAND rn on rn.nav_strand_id = rc.NAV_STRAND_ID
                    where rc.condition_type = 7
                    group by rn.NAV_STRAND_ID
                    having count (*) = 2
                )
            )
select distinct snl.LINK_ID, sl.STUB_ID
    from STUB_NAV_LINK snl
    join STUB_LINK sl on snl.LINK_ID = sl.LINK_ID
    where
        -- Part of TRANSITION_MASK
         exists (
            select 1
            from CF_PSF_STUB_LINK_RESTR_MNR crm
            where (snl.LINK_ID = crm.TO_LINK_ID or snl.LINK_ID = crm.FROM_LINK_ID)
                and crm.NODE_ID in (select NODE_ID from SC_BORDER_NODE)
        )

