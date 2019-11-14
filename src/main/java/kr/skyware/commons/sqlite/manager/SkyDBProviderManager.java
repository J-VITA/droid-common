package kr.skyware.commons.sqlite.manager;

import android.content.Context;
import android.database.Cursor;

import kr.skyware.commons.data.SkyUser;
import kr.skyware.commons.data.exception.NoSuchUserException;
import kr.skyware.commons.sqlite.SkyDBData;

public class SkyDBProviderManager implements SkyDBData {

    private static SkyDBProviderManager sInstance;

    private Context context;

    public static final SkyDBProviderManager getInstance(Context context) {
        if (sInstance == null) {
            return new SkyDBProviderManager(context);
        }
        return sInstance;
    }

    public SkyDBProviderManager(Context context) {
        this.context = context;
    }

    public SkyUser findUserById(String userId) throws NoSuchUserException {
        Cursor cursor = context.getContentResolver().query(URI_USER, null, COLUMN_IDENTITY + "=?", new String[] { userId }, null);
        cursor.moveToFirst();

        SkyUser user = new SkyUser();

        if (cursor != null && cursor.getCount() > 0) {
            user.setId(cursor.getString(cursor.getColumnIndex(COLUMN_IDENTITY)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)));
            user.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_AGE)));
            user.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY)));
            user.setMdn(cursor.getString(cursor.getColumnIndex(COLUMN_MDN))); // PhoneNumber
            user.setIdx(cursor.getString(cursor.getColumnIndex(COLUMN_IDXCODE)));
        } else {
            throw new NoSuchUserException();
        }

        return user;
    }
}