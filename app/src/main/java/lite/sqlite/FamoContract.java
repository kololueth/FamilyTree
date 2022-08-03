package lite.sqlite;

import android.provider.BaseColumns;

public final class FamoContract {

    private FamoContract(){} // End of Constructor


    public static class Entry implements BaseColumns {

        public static final String USER_TABLE = "user";
        public static final String MEMBER_TABLE = "member";
        public static final String UNIT_TABLE = "unit";


        public static final String FIRST_NAME_COLUMN = "first_name";
        public static final String MIDDLE_NAME_COLUMN = "middle_name";
        public static final String LAST_NAME_COLUMN = "last_name";
        public static final String BIRTHDATE_COLUMN = "birthdate";
        public static final String SEX_COLUMN = "sex";

        public static final String UNIT_ID = "fuid";
        public static final String MEMBER_ID = "mid";
        public static final String MEMBER_TYPE = "type";  // 0 = parent, 1 = child

    }  // End of class

    static final String SQL_CREATE_USERS = "CREATE TABLE " + Entry.USER_TABLE
            + " (" + Entry._ID + " INTEGER PRIMARY KEY, "
            + Entry.FIRST_NAME_COLUMN + " TEXT, "
            + Entry.MIDDLE_NAME_COLUMN + " TEXT, "
            + Entry.LAST_NAME_COLUMN + " TEXT, "
            + Entry.BIRTHDATE_COLUMN + " TEXT, "
            + Entry.SEX_COLUMN + " TEXT)";

    static final String SQL_CREATE_MEMBERS = "CREATE TABLE " + Entry.MEMBER_TABLE
            + " (" + Entry._ID + " INTEGER PRIMARY KEY, "
            + Entry.FIRST_NAME_COLUMN + " TEXT, "
            + Entry.MIDDLE_NAME_COLUMN + " TEXT, "
            + Entry.LAST_NAME_COLUMN + " TEXT, "
            + Entry.BIRTHDATE_COLUMN + " TEXT, "
            + Entry.SEX_COLUMN + " TEXT)";

    static final String SQL_CREATE_UNITS = "CREATE TABLE " + Entry.UNIT_TABLE
            + " (" + Entry.UNIT_ID + " INTEGER, "
            + Entry.MEMBER_ID + " INTEGER, "
            + Entry.MEMBER_TYPE + " INTEGER)";


    static final String SQL_DELETE_MEMBERS = "DROP TABLE IF EXISTS " + Entry.MEMBER_TABLE;
    static final String SQL_DELETE_UNITS = "DROP TABLE IF EXISTS " + Entry.UNIT_TABLE;


} // End of class
