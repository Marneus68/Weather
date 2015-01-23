package fr.iridia.weather.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "ActionBarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSearch(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition (R.anim.left_entering_scroll, R.anim.right_leaving_scroll);
    }

    public void openSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openHere(View v) {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            enableGPSPopup();
        } else {
            openWeatherDetailsActivity();
        }
    }

    public void openWeatherDetailsActivity() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isAvailable()
                || !conMgr.getActiveNetworkInfo().isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.data_disabled)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }

        Intent intent = new Intent(this, WeatherDetailsActivity.class);
        intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_KEY, WeatherDetailsActivity.DETAIL_ACTIVITY_TYPES.HERE);
        startActivity(intent);
    }

    protected void enableGPSPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.gps_disabled)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        openWeatherDetailsActivity();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
