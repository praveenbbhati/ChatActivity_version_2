package com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/***
 * This class is responsible to handle all shared preference related functionality
 **/
public class AppSharedPreferences {
    private static SharedPreferences sSharedPreferences;
    private static Editor editor;
    private static final String PREF_NAME = "loginDetailsSharedPref";
    private static AppSharedPreferences uniqueInstance;

    @SuppressLint("CommitPrefEdits")
    public static synchronized AppSharedPreferences getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new AppSharedPreferences();

            sSharedPreferences = SharedApplication.getApplicationInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = sSharedPreferences.edit();
        }
        return uniqueInstance;
    }

    public static void writeString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public static String readString(String key) {
        return sSharedPreferences.getString(key, null);
    }

    public static void writeInteger(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public static int readInteger(String key) {
        return sSharedPreferences.getInt(key, 0);
    }

    public static String readString(String key, String defaultValue) {
        return sSharedPreferences.getString(key, defaultValue);
    }

    public static void writeBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public static boolean readBoolean(String key) {
        return sSharedPreferences.getBoolean(key, false);
    }

    public boolean clearAll() {
        return editor.clear().commit();
    }
}
