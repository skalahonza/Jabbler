package eu.alavio.jabbler.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Static settings helper for safe access to SharedPreferences
 */
public final class SettingsHandler {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public static Boolean getConvertEmoticons(Context context) {
        initReader(context);
        return sp.getBoolean("convertEmoticons",true);
    }

    public static void setConvertEmoticons(Boolean convertEmoticons, Context context) {
        initWriter(context);
        editor.putBoolean("convertEmoticons",convertEmoticons);
        editor.commit();
    }

    public static Boolean getLogInAutomatically(Context context) {
        initReader(context);
        return sp.getBoolean("logInAutomatically",true);
    }

    public static void setLogInAutomatically(Boolean logInAutomatically, Context context) {
        initWriter(context);
        editor.putBoolean("logInAutomatically",logInAutomatically);
        editor.commit();
    }

    public static Boolean getBackgroundNotifications(Context context) {
        initReader(context);
        return sp.getBoolean("backgroundNotifications",true);
    }

    public static void setBackgroundNotifications(Boolean backgroundNotifications, Context context) {
        initWriter(context);
        editor.putBoolean("backgroundNotifications",backgroundNotifications);
        editor.commit();
    }

    public static Boolean getNotificationVibrations(Context context) {
        initReader(context);
        return sp.getBoolean("notificationVibrations",true);
    }

    public static void setNotificationVibrations(Boolean notificationVibrations, Context context) {
        initWriter(context);
        editor.putBoolean("notificationVibrations",notificationVibrations);
        editor.commit();
    }

    public static String getNotificationSound(Context context){
        initReader(context);
        return sp.getString("notificationSound", "DEFAULT_SOUND");
    }

    private static void initReader(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static void initWriter(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();

    }
}