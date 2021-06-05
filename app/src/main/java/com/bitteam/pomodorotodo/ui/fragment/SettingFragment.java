package com.bitteam.pomodorotodo.ui.fragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;

import com.bitteam.pomodorotodo.R;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_setting, rootKey);
    }
}
