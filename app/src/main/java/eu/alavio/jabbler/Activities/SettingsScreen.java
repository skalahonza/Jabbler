package eu.alavio.jabbler.Activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import eu.alavio.jabbler.R;

/**
 * Fragment holding settings view
 */

public class SettingsScreen extends PreferenceActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);
    }
}
