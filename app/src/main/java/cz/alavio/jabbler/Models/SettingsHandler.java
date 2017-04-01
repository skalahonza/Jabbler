package cz.alavio.jabbler.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jan Skála on 30.03.2017.
 */

public final class SettingsHandler {
    public static Boolean getConvertEmoticons() {
        return convertEmoticons;
    }

    public static void setConvertEmoticons(Boolean convertEmoticons) {
        SettingsHandler.convertEmoticons = convertEmoticons;
    }

    public static Boolean getLogInAutomatically() {
        return logInAutomatically;
    }

    public static void setLogInAutomatically(Boolean logInAutomatically) {
        SettingsHandler.logInAutomatically = logInAutomatically;
    }

    public static Boolean getBackgroundNotifications() {
        return backgroundNotifications;
    }

    public static void setBackgroundNotifications(Boolean backgroundNotifications) {
        SettingsHandler.backgroundNotifications = backgroundNotifications;
    }

    public static Boolean getNotificationVibrations() {
        return notificationVibrations;
    }

    public static void setNotificationVibrations(Boolean notificationVibrations) {
        SettingsHandler.notificationVibrations = notificationVibrations;
    }

    private static Boolean convertEmoticons;
    private static Boolean logInAutomatically;
    private static Boolean backgroundNotifications;
    private static Boolean notificationVibrations;


    private static SharedPreferences sp;

    private static void initSettings(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setByKey(String key, Object data ,Context context){
        initSettings(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,data.toString());
        editor.apply();
    }
}
