package com.skts.ourmemory.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    /* SharedPreferences key*/
    private static final String PREFERENCE_NAME = "sharedPreferences";
    
    private static MySharedPreferences mMySharedPreferences = null;
    private static Context mContext;
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mEditor;

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;

        if (mMySharedPreferences == null) {
            mMySharedPreferences = new MySharedPreferences();
        }

        if (mPrefs == null) {
            mPrefs = mContext.getSharedPreferences(PREFERENCE_NAME, context.MODE_PRIVATE);
            mEditor = mPrefs.edit();
        }

        return mMySharedPreferences;
    }

    public void putIntExtra(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void putStringExtra(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void putLongExtra(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void putBooleanExtra(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public int getIntExtra(String key) {
        return mPrefs.getInt(key, 0);
    }

    public String getStringExtra(String key) {
        return mPrefs.getString(key, "");
    }

    public long getLongExtra(String key) {
        return mPrefs.getLong(key, 0);
    }

    public boolean getBooleanExtra(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void removePreference(String key) {
        mEditor.remove(key).commit();
    }

    public boolean containCheck(String key) {
        return mPrefs.contains(key);
    }
}
