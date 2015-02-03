package fr.iridia.weather.weather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void clearSettings(View v) {

    }
}
