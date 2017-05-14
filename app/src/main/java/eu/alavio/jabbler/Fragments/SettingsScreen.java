package eu.alavio.jabbler.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import eu.alavio.jabbler.R;

/**
 * Created by Jan Skála on 01.04.2017.
 */

public class SettingsScreen extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);
    }
}
