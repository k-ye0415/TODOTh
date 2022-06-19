package com.ioad.todoth.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Shared {

    public static SharedPreferences preferences;


    public static void setCheckBoxPref(Context context, String key, boolean value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getCheckBoxPref(Context context, String key) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }

}
