package fr.iridia.weather.weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.iridia.weather.weather.data.LocationWeatherInfo;

public class MainActivity extends Activity {

    public static final String TAG                  = "MainActivity";

    public static final String PreferencesString    = "WeatherPreferences";
    public static final String PrefFirstLaunchKey   = "FirstLaunch";
    public static final String PrefFavCitiesKey     = "FavCities";

    public static ArrayList<LocationWeatherInfo> favCities = null;

    ListView favListView;

    public static ArrayList<LocationWeatherInfo> SetToArrayList(Set<String> set) {
        ArrayList<LocationWeatherInfo> aa = new ArrayList<LocationWeatherInfo>();
        for(String s : set) {
            try {
                LocationWeatherInfo l = new LocationWeatherInfo(
                        s.split("\\|")[0],
                        Double.valueOf(s.split("\\|")[2]),
                        Double.valueOf(s.split("\\|")[1]));
                aa.add(l);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return aa;
    }

    public static ArrayList<LocationWeatherInfo> ArrayToArrayList(String[] arr) {
        ArrayList<LocationWeatherInfo> aa = new ArrayList<LocationWeatherInfo>();
        for(String s : arr) {
            try {
                LocationWeatherInfo l = new LocationWeatherInfo(
                        s.split("\\|")[0],
                        Double.valueOf(s.split("\\|")[2]),
                        Double.valueOf(s.split("\\|")[1]));
                aa.add(l);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return aa;
    }

    protected void refreshFavList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferencesString, Context.MODE_PRIVATE);

        if (null == favCities)
            favCities = new ArrayList<>();
        else
            favCities.clear();

        if (!sharedPreferences.contains(PrefFirstLaunchKey)) {

            String[] defaultCities = getResources().getStringArray(R.array.default_cities);
            SharedPreferences.Editor ed = sharedPreferences.edit();
            HashSet<String> hs = new HashSet<String>();
            for(String s : defaultCities) {
                hs.add(s);
            }
            ed.putStringSet(PrefFavCitiesKey, hs);
            ed.putBoolean(PrefFirstLaunchKey, true);
            ed.apply();

            favCities = ArrayToArrayList(defaultCities);
        } else {
            HashSet<String> hs = (HashSet<String>) sharedPreferences.getStringSet(PrefFavCitiesKey, null);
            if (hs == null) {
                String[] defaultCities = getResources().getStringArray(R.array.default_cities);
                SharedPreferences.Editor ed = sharedPreferences.edit();
                hs = new HashSet<String>();
                for(String s : defaultCities) {
                    hs.add(s);
                }
                ed.putStringSet(PrefFavCitiesKey, hs);
                ed.putBoolean(PrefFirstLaunchKey, true);
                ed.apply();

                favCities = ArrayToArrayList(defaultCities);
            } else {
                favCities = SetToArrayList(hs);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        refreshFavList();

        Log.i(TAG, favCities.toString());

        favListView = (ListView) findViewById(R.id.favListView);
        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, WeatherDetailsActivity.class);
                intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_KEY, WeatherDetailsActivity.DETAIL_ACTIVITY_TYPES.NAMED_GPS_COORDINATES);
                intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_KEY, favCities.get(position).name);
                intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_LATITUDE, favCities.get(position).latitude);
                intent.putExtra(WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_LONGITUDE, favCities.get(position).longitude);
                startActivity(intent);
            }
        });
        favListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFavList();
        favListView.setAdapter(new ArrayAdapterFavCity(this, R.layout.fav_row, favCities));
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
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
