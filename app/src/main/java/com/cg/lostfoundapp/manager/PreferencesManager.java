package com.cg.lostfoundapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.cg.lostfoundapp.utils.EncryptUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gabi on 9/10/2016.
 */
public class PreferencesManager {

    private static final String PREF_NAME = "lf_pref";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();

    }



    public PreferencesManager setString(String key, String value) {
        if (value!=null) {
            editor.putString(key, value);
        } else {
            editor.remove(key);
        }
        return this;
    }


    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public PreferencesManager setStringSecure(String key, String value) {
        if (value!=null) {
            String encryptedValue = EncryptUtils.getInstance().encryptValue(value);
            return setString(key, encryptedValue);
        }
        return setString(key, value);
    }

    public String getStringSecure(String key) {
        String value = getString(key);
        return value!=null? EncryptUtils.getInstance().decrypt(value) : null;
    }

    public PreferencesManager setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    public boolean getBoolean(String key) {
        return  sharedPreferences.getBoolean(key, false);
    }


    public PreferencesManager setInteger(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    public int getInteger(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public PreferencesManager setFloat(String key, float value) {
        editor.putFloat(key, value);
        return this;
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public float getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }



    public PreferencesManager setLong(String key, long value) {
        editor.putLong(key, value);
        return this;
    }

    public PreferencesManager remove(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key);
        }
        return this;
    }

    public void commit() {
        editor.commit();
    }

    public void listAll() {
        Map<String, ?> prefMap = sharedPreferences.getAll();
        Set<String> keys = new HashSet<String>(prefMap.keySet());
        for (String key : keys) {
            System.out.println(key + " " + prefMap.get(key));
        }
    }



}
