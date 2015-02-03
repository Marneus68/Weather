package fr.iridia.weather.weather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void clearSettings(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PreferencesString, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.remove(MainActivity.PrefFirstLaunchKey);
        ed.apply();
    }
}
