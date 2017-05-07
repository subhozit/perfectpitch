package com.medias.perfectpitch.data;

import android.content.Context;
import android.util.Log;

import com.medias.perfectpitch.utils.SecuredSharedPreferences;

public class Preferences {
    private static SecuredSharedPreferences securedSharedPreferences;
    public static final int TRUE = 1;
    private static final int DEFAULT = 0;

    public static final int TONES_PURCHASED = 0;
    public static final int TWELVE_NOTES_PURCHASED = 1;
    public static final int NOTE_NAMING_ID = 2;
    public static final int RELEASE_ID = 3;
    public static final int TONE_ID = 4;
    public static final int SYNTH_TONE_ID = 5;
    public static final int HIGHEST_SCORE = 6;
    public static final int USER_MAIL = 7;
    public static final int AUTO_PLAY_ORDER = 8;


    public static int get(Context context, int preferenceId) {

        if (securedSharedPreferences == null) {
            securedSharedPreferences = new SecuredSharedPreferences(context, context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE));
        }
        int i = securedSharedPreferences.getInt(String.valueOf(preferenceId), DEFAULT);
        //Log.i("data.Preferences", "get=" + preferenceId + ":" + i);
        return i;
    }

    public static void set(Context context, int preferenceId, int value) {
        //Log.i("data.Preferences", "set=" + preferenceId + ":" + value);
        if (securedSharedPreferences == null) {
            securedSharedPreferences = new SecuredSharedPreferences(context, context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE));
        }
        securedSharedPreferences.edit().putInt(String.valueOf(preferenceId), value).apply();
    }

    public static void setString(Context context, int preferenceId, String value) {
        if (securedSharedPreferences == null) {
            securedSharedPreferences = new SecuredSharedPreferences(context, context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE));
        }
        securedSharedPreferences.edit().putString(String.valueOf(preferenceId), value).apply();
    }

    public static String getString(Context context, int preferenceId) {
        if (securedSharedPreferences == null) {
            securedSharedPreferences = new SecuredSharedPreferences(context, context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE));
        }
        return securedSharedPreferences.getString(String.valueOf(preferenceId), "");
    }
}
