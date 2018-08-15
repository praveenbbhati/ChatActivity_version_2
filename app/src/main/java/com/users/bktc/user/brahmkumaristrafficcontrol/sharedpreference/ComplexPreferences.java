package com.users.bktc.user.brahmkumaristrafficcontrol.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Manas on 02-01-2018.
 */

public class ComplexPreferences {

    private static ComplexPreferences complexPreferences;
    private static Gson GSON = new Gson();
    private static final String PREF_NAME = "PayProPreferences";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ComplexPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static ComplexPreferences getComplexPreferences(Context context) {
        if (complexPreferences == null) {
            complexPreferences = new ComplexPreferences(context);
        }
        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }
        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("key is empty or null");
        }
        editor.putString(key, GSON.toJson(object)).commit();
    }

    public void clearObject() {
        editor.clear();
    }

    public <T> T getObject(String key, Class<T> a) {
        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storage with key " + key + " is instanceof other class");
            }
        }
    }
}