package kr.skyware.commons;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SkyPreSharedRef {
    //
    public static final String PREF_NAME = "SKYWARE_PRE_SHARED_REF";

    private static SkyPreSharedRef sInstance;
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SkyPreSharedRef(Context context){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    //외부 생성자 싱글톤
    public static synchronized SkyPreSharedRef getInstance(Context context){
        if(sInstance == null){
            sInstance = new SkyPreSharedRef(context);
        }
        return sInstance;
    }

    /////////////////////////set value s/////////////////////////
    public void put(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }


    public void put(String key, List<Object> messages){
        Gson gson = new Gson();
        String json = gson.toJson(messages);

        editor.putString(key, json);
        editor.commit();
    }

    public void put(String key, Set<String> set){
        editor.putStringSet(key, set);
        editor.commit();
    }
    /////////////////////////set value e/////////////////////////

    /////////////////////////get value s/////////////////////////
    public String getValue(String key, String defaultValue) {
        try {
            return pref.getString(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getValue(String key, int defaultValue) {
        try {
            return pref.getInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getValue(String key, boolean defaultValue) {
        try {
            return pref.getBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Set<String> getValue(String key) {
        try {
            return pref.getStringSet(key, null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object> getValue(String key, String listStr, String defaultValue) {
        try {
            Gson gson = new Gson();
            if( listStr.isEmpty() ){
                return new ArrayList<>();
            } else{
                String jsonStr = getValue(listStr, "");
                Type type = new TypeToken<List<Object>>() {
                }.getType();
                return gson.fromJson(jsonStr, type);
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    /////////////////////////get value e/////////////////////////

    /////////////////////////clear value s/////////////////////////
    public void clearValue(String key, Object value){
        if (value instanceof String){
            editor.putString(key, "");
        }
        if (value instanceof Integer){
            editor.putInt(key, -1);
        }
        if (value instanceof Boolean){
            editor.putBoolean(key, false);
        }
        editor.apply();
    }
    /////////////////////////clear value e/////////////////////////
}
