package kr.skyware.commons.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import kr.skyware.commons.util.Globals;
import kr.skyware.commons.util.Logging;

public class SkyDBHelper extends SQLiteOpenHelper implements SkyDBData {

    private static SkyDBHelper sInstance;

    private SkyDBHelper(Context context, String dbname2, CursorFactory factory, int i) {
        super(context, dbname2, factory, i);
    }

    public static final SkyDBHelper getInstance(Context context) {
        if (sInstance == null) {
            return new SkyDBHelper(context, DBNAME, null, 1); // 마지막 인자인 숫자가 커지도록 수정하면 버전업이 되어 onUpgrade()가 호출됨. // db_version
        }
        return sInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase argDB) {
        // TODO :
//		argDB.execSQL("DROP TABLE IF EXISTS 'user'");
//		argDB.execSQL("DROP TABLE IF EXISTS 'history'");
//		onCreate(argDB);
    }

    @Override
    public void onCreate(SQLiteDatabase argDB) { // 처음 DB를 생성할 때 호출됨.(CREATE TABLE)
        argDB.execSQL(SQL_USER_TABLE);

        for (String[] initValue : INIT_USER_DATA) {
            try {
                argDB.execSQL("INSERT INTO " + TBNAME_USER + "(" + COLUMN_IDENTITY + "," + COLUMN_PASSWORD + ","
                        + COLUMN_USERNAME + "," + COLUMN_GENDER + "," + COLUMN_AGE + "," + COLUMN_BIRTHDAY + ","
                        + COLUMN_MDN + "," + COLUMN_IDXCODE +")"
                        + " VALUES(" + "'" + initValue[0] + "','" + initValue[1]
                        + "','" + initValue[2] + "','" + initValue[3] + "','" + initValue[4] + "','" + initValue[5]
                        + "','" + initValue[6] + "','" + initValue[7] +"'); ");
            } catch (SQLException e) {
                Logging.d(Globals.LOG_TAG, e.getMessage());
            } catch (Exception e) {
                Logging.d(Globals.LOG_TAG, e.getMessage());
            }
        }
        Logging.d(Globals.LOG_TAG, SQL_USER_TABLE);

        argDB.execSQL(SQL_HISTORY_TABLE);
        Logging.d(Globals.LOG_TAG, SQL_HISTORY_TABLE);
        for (String[] initHistoryValue : INIT_HISTORY_DATA) {
            argDB.execSQL("INSERT INTO " + TBNAME_HISTORY + "(" + COLUMN_IDENTITY  + "," + COLUMN_ACTIVITY + "," + COLUMN_COM_TIME + "," + COLUMN_HST_IDXCODE +")"
                    + " VALUES(" + "'" + initHistoryValue[0] + "','" + initHistoryValue[1] + "','"+ initHistoryValue[2] + "','" + initHistoryValue[3]  + "'); ");
        }
        Logging.d(Globals.LOG_TAG, "onCreate called, DB Created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase argDB, int oldVersion, int newVersion) {
        argDB.execSQL("DROP TABLE IF EXISTS 'user'");
        argDB.execSQL("DROP TABLE IF EXISTS 'history'");

        onCreate(argDB);
        Logging.d(Globals.LOG_TAG, "onCreate called, DB Upgraded");
    }

}