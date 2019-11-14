package kr.skyware.commons.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import kr.skyware.commons.util.Globals;
import kr.skyware.commons.util.Logging;

public class SkyDBProvider extends ContentProvider implements SkyDBData {

    public SkyDBHelper dbHelper;

    public SkyDBProvider() {
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = SkyDBHelper.getInstance(getContext());
        if (this.dbHelper == null)
            return false;

        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        long rowId = db.insert(uri.getPath().substring(1), null, values);
        if (rowId < 0) {
            Logging.d(Globals.LOG_TAG, "DBProvider insert() -> null");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Logging.d(Globals.LOG_TAG, "DBProvider insert() -> return Uri");
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        int nRows = db.delete(uri.getPath().substring(1), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return nRows;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.query(uri.getPath().substring(1), projection, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        int nRes = 0;
        try {
            nRes = db.update(uri.getPath().substring(1), values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nRes;
    }

}