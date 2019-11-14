package kr.skyware.commons.sqlite;

import android.net.Uri;

public interface SkyDBData {    // 전체 DB 데이터라기 보다는 초기데이터임.
    public static final String DBNAME = "skyware-drooid-commons.db";
    public static final String AUTHORITY = "kr.skyware.commons";

    public static final String TBNAME_USER = "user";                   // User Table Name
    public static final String TBNAME_SETTINGS = "settings";           // Settings Table Name
    public static final String TBNAME_HISTORY = "history";
    public static final String DB_ID_COLUMN = "_id";

    // USER_TABLE
    public static final String COLUMN_IDENTITY = "uid";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERNAME = "name";
    public static final String COLUMN_GENDER   = "gender";
    public static final String COLUMN_AGE      = "age";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_MDN      = "mdn";                 //Phone Number
    public static final String COLUMN_IDXCODE  = "idx";

    public static String SQL_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TBNAME_USER + "("
            + DB_ID_COLUMN 	  + " INTEGER PRIMARY KEY, " // PK
            + COLUMN_IDENTITY + " TEXT, "                // uid
            + COLUMN_PASSWORD + " TEXT, "                // password
            + COLUMN_USERNAME + " TEXT, "                // name
            + COLUMN_GENDER   + " TEXT, "                // gender
            + COLUMN_AGE      + " TEXT, "                // age
            + COLUMN_BIRTHDAY + " TEXT, "                // birthday
            + COLUMN_MDN      + " TEXT, "                // mdn
            + COLUMN_IDXCODE  + " TEXT ) ";              // 사용자 식별 해시코드

    /**
     * 테이블 생성 SQL에서 컬럼명과 TEXT(자료형)가 빈칸없이 더해지면 sqlite가 합쳐진것을 컬럼명으로 인식함. 빈칸 유의
     * */

    public static String[][] INIT_USER_DATA = {
            { "admin",  "skyware2019",  "skyware",   "M", "11", "2019.01.01", "010-1111-1111", null},
            { "jaykim", "1234",         "김정중",    "M", "32", "1988.08.13", "010-5342-9905", null}
    };

    // HISTORY_TABLE
    public static final String COLUMN_ACTIVITY     = "activity";
    public static final String COLUMN_HST_IDXCODE = "idx";
    public static final String COLUMN_COM_TIME = "click_date";

    public static final String SQL_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TBNAME_HISTORY + "("
            + DB_ID_COLUMN       + " INTEGER PRIMARY KEY, " // 인덱스
            + COLUMN_IDENTITY    + " TEXT, "                // 사용자 아이디
            + COLUMN_ACTIVITY     + " TEXT, "               // 접속화면명
            + COLUMN_COM_TIME    + " TEXT, "                // 접속시간
            + COLUMN_HST_IDXCODE + " TEXT )";               // 해시코드

    public static String[][] INIT_HISTORY_DATA = {
            { "admin",   "MainActivity",  "2019-11-14T00:00", null},
            { "jaykim",  "IntroActivity", "2019-11-14T00:00", null}
    };


    // URI
    public static final Uri URI_USER = Uri.parse("content://" + AUTHORITY + "/" + TBNAME_USER);
    public static final Uri URI_HISTORY = Uri.parse("content://" + AUTHORITY + "/" + TBNAME_HISTORY);
}
