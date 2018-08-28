package io.github.gao23.myapplication.Logic;

/**
 * Majority of this code is not my own, I pulled it from http://blog.nkdroidsolutions.com/class-object-in-sharedpreferences/ and modified it to suit my needs.
 * All due credits go to the original author.
 */

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


import java.util.List;

/**
 *  This is the library for saving to jason
 *  modified the token to match the store object
 */
public class ComplexPreferences {

    private static ComplexPreferences complexPreferences;
    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<List<Entry>>() {
    }.getType();
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        this.context = context;
        if (namePreferences == null || namePreferences.equals("")) {
            namePreferences = "complex_preferences";
        }
        preferences = context.getSharedPreferences(namePreferences, mode);
        editor = preferences.edit();
    }

    public static ComplexPreferences getComplexPreferences(Context context, String namePreferences, int mode) {

//      if (complexPreferences == null) {
        complexPreferences = new ComplexPreferences(context,
                namePreferences, mode);
//      }

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.putString(key, GSON.toJson(object));
    }

    public void commit() {
        editor.commit();
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
                return GSON.fromJson(gson, typeOfObject);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }


}