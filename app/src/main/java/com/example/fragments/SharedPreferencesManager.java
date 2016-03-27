package com.example.fragments;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String SP_AUTH_INFO = "auth_info";
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";

    private static String get(String filename, String keyname) {
        SharedPreferences sPref;
        sPref = ConfigManager.getApplicationContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sPref.getString(keyname, null);
    }

    private static void set(String filename, String keyname, String value) {
        SharedPreferences sPref;
        sPref = ConfigManager.getApplicationContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(keyname, value);
        ed.apply();
    }

    public static String getAccessToken() {
        return get(SP_AUTH_INFO, KEY_ACCESS_TOKEN);
    }

    public static void setAccessToken(String token) {
        set(SP_AUTH_INFO, KEY_ACCESS_TOKEN, token);
    }
}