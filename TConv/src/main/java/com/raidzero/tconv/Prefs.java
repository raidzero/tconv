package com.raidzero.tconv;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by raidzero on 9/13/13 7:43 PM
 */
public class Prefs extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
