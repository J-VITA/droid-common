package kr.skyware.commons.sqlite.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SkyHistoryDBManager {
    static final String DBNAME = "skyware-drooid-commons.db";
    static final String TABLE_HISTOY = "history";
    static final int DB_VERSION = 1; // db_version

    public static final String DB_ID_COLUMN = "_id";

    // HISTORY_TABLE
    public static final String COLUMN_IDENTITY = "uid";
    public static final String COLUMN_ACTIVITY = "activity";
    public static final String COLUMN_COM_TIME = "click_date";
    public static final String COLUMN_HST_IDXCODE = "idx";


    Context mContext = null;

    private static SkyHistoryDBManager prescriptionDBManager = null;
    private SQLiteDatabase db = null;

    public static SkyHistoryDBManager getInstance(Context context){
        if( prescriptionDBManager == null) {
            prescriptionDBManager = new SkyHistoryDBManager(context);
        }
        return prescriptionDBManager;
    }

    private SkyHistoryDBManager (Context context){
        mContext = context;

        db = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HISTOY + " ("
                + DB_ID_COLUMN       + " INTEGER PRIMARY KEY, " // 인덱스
                + COLUMN_IDENTITY    + " TEXT, "                // 사용자 아이디
                + COLUMN_ACTIVITY    + " TEXT, "                // 접속화면명
                + COLUMN_COM_TIME    + " TEXT, "                // 접속시간
                + COLUMN_HST_IDXCODE + " TEXT )"                // 인덱스코드
        );
    }

    public long insert (ContentValues addRowValues){
        return db.insert(TABLE_HISTOY, null, addRowValues);
    }

    public Cursor query (String [] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return db.query(TABLE_HISTOY, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public long replace (ContentValues addRowValues){
        return db.replace(TABLE_HISTOY, null, addRowValues);
    }

    public int update (ContentValues updateRowValue, String whereClause, String[] whereArgs){
        return db.update(TABLE_HISTOY, updateRowValue, whereClause, whereArgs);
    }

    public int delete(String whereClause, String[] whereArgs){
        return db.delete(TABLE_HISTOY, whereClause, whereArgs);
    }

    public void directQuery(String query){
        db.execSQL(query);
    }
}
