package kr.skyware.commons.sqlite.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kr.skyware.commons.util.Globals;
import kr.skyware.commons.util.Logging;

public class SkyUserDBManager {
    static final String DBNAME = "skyware-drooid-commons.db";
    static final String TABLE_USER = "user";
    static final int DB_VERSION = 1; // db_version


    // USER_TABLE
    public static final String DB_ID_COLUMN = "_id";
    public static final String COLUMN_IDENTITY = "uid";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERNAME = "name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_MDN = "mdn";
    public static final String COLUMN_IDXCODE = "idx";

    Context mContext = null;

    private static SkyUserDBManager skyUserDBManager = null;
    private SQLiteDatabase db = null;

    public static SkyUserDBManager getInstance(Context context){
        if( skyUserDBManager == null) {
            skyUserDBManager = new SkyUserDBManager(context);
        }
        return skyUserDBManager;
    }

    private SkyUserDBManager (Context context){
        mContext = context;

        db = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ("
                + DB_ID_COLUMN    + " INTEGER PRIMARY KEY, " // 인덱스
                + COLUMN_IDENTITY + " TEXT, "                // 사용자 아이디
                + COLUMN_PASSWORD + " TEXT, "                // 사용자 패스워드
                + COLUMN_USERNAME + " TEXT, "                // 사용자 이름
                + COLUMN_GENDER   + " TEXT, "                // 성별
                + COLUMN_AGE      + " TEXT, "                // 나이
                + COLUMN_BIRTHDAY + " TEXT, "                // 생일
                + COLUMN_MDN      + " TEXT, "                // 전화번호
                + COLUMN_IDXCODE  + " TEXT ) "               // 사용자 식별 해시코드
        );

		Logging.d(Globals.LOG_TAG, "SkyUserDBManager constructor called");
    }

    public long insert (ContentValues addRowValues){
        return db.insert(TABLE_USER, null, addRowValues);
    }

    public Cursor query (String [] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return db.query(TABLE_USER, columns, selection, selectionArgs, groupBy, having, orderBy);
        /*
         * columns 인자   : 검색결과로 얻게될 컬럼명, new String[] {"id", "password", "name", "gender", "age", "birthday", "height", "weight", "mdn"}
         * selection 인자 : 검색조건항을 설정 id가 id07 이면 id = 'id07' 로 설정
         * selectionArgs 인자 : id = ?, password = ? 물음표 개수로 String[] 과 대응
         */
    }

    public int update (ContentValues updateRowValue, String whereClause, String[] whereArgs){
        return db.update(TABLE_USER, updateRowValue, whereClause, whereArgs);
    }

    public int delete(String whereClause, String[] whereArgs){
        return db.delete(TABLE_USER, whereClause, whereArgs);
    }
}
