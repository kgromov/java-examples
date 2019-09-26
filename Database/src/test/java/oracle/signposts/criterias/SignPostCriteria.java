package oracle.signposts.criterias;

public enum SignPostCriteria implements ICriteria{

    OFFSET {
        @Override
        public String getQuery() {
            return  "select FILE_ID from CDC_RDF_FILE_2D_SIGN " +
                    "where REGEXP_LIKE (utl_raw.cast_to_varchar2(dbms_lob.substr(FILE_OBJECT, 2000, 2001)), '(dx)|(dy)')";
        }
    },
    LETTER_SPACING {
        @Override
        public String getQuery() {
            return  "select * from CDC_RDF_FILE_2D_SIGN " +
                    "where REGEXP_LIKE ( utl_raw.cast_to_varchar2(dbms_lob.substr(FILE_OBJECT, 2000, 2001)), 'letter-spacing')";
        }
    },
}
