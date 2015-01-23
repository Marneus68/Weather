package fr.iridia.weather.weather;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import fr.iridia.weather.weather.data.LocationWeatherInfo;
import fr.iridia.weather.weather.data.QuerryLocation;

public class WeatherDetailsActivity extends ActionBarActivity {

    public static final String TAG = "ActionBarActivity";

    public static final String  DETAIL_ACTIVITY_KEY             = "EXTRA_DETAIL_ACTIVITY_TYPE";
    public static final String  DETAIL_ACTIVITY_EXTRA_KEY       = "EXTRA_DETAIL_ACTIVITY_EXTRA_KEY";
    public static final String  DETAIL_ACTIVITY_EXTRA_LONGITUDE = "EXTRA_DETAIL_ACTIVITY_LONGITUDE";
    public static final String  DETAIL_ACTIVITY_EXTRA_LATITUDE  = "EXTRA_DETAIL_ACTIVITY_LATITUDE";
    public static final class   DETAIL_ACTIVITY_TYPES {
        public static final String HERE                         = "here";
        public static final String NAMED_CITY                   = "named_city";
        public static final String NAMED_GPS_COORDINATES        = "named_gps_coordinates";

        private  DETAIL_ACTIVITY_TYPES() {}
    }

    TextView tvName;
    TextView tvGPS;
    TextView tvTemp;
    ImageView ivWeather;

    String packagename = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        packagename = getApplicationContext().getPackageName();

        tvName  = (TextView) findViewById(R.id.detailsTitle);
        tvGPS   = (TextView) findViewById(R.id.detailsGPS);
        tvTemp  = (TextView) findViewById(R.id.detailsTemperature);
        ivWeather = (ImageView) findViewById(R.id.detailsIcon);

        tvGPS.setText(R.string.unset);
        tvTemp.setText(R.string.unset);

        switch (getIntent().getStringExtra(DETAIL_ACTIVITY_KEY)) {
            case DETAIL_ACTIVITY_TYPES.HERE:
                tvName.setText(R.string.here);

                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        hereDetails(location);
                    }
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    public void onProviderEnabled(String provider) {}
                    public void onProviderDisabled(String provider) {}
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                break;
            case DETAIL_ACTIVITY_TYPES.NAMED_CITY:
                tvName.setText(getIntent().getStringExtra(DETAIL_ACTIVITY_EXTRA_KEY));
                tvGPS.setText("");
                break;
            case DETAIL_ACTIVITY_TYPES.NAMED_GPS_COORDINATES:
                tvName.setText(getIntent().getStringExtra(DETAIL_ACTIVITY_EXTRA_KEY));
                double tmpLongitude = getIntent().getDoubleExtra(DETAIL_ACTIVITY_EXTRA_LONGITUDE, 0);
                double tmpLatitude = getIntent().getDoubleExtra(DETAIL_ACTIVITY_EXTRA_LATITUDE, 0);
                tvGPS.setText(tmpLatitude + " : " + tmpLongitude);

                new OpenWeatherMapGPSAsyncTask() {
                    @Override
                    protected void onPostExecute(LocationWeatherInfo result) {
                        if (result!=null) {
                            tvTemp.setText(String.valueOf((int) Math.round(result.todayWeatherInfo.temperature))+"°");
                            ivWeather.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("w" + result.todayWeatherInfo.image, "drawable", packagename)));
                        }
                    }
                }.execute(new QuerryLocation(tmpLongitude, tmpLatitude));

                break;
        }
    }

    protected void hereDetails(Location location) {
        tvGPS.setText(location.getLatitude() + " : " + location.getLongitude());

        final WeatherDetailsActivity wda = this;

        new OpenWeatherMapGPSAsyncTask() {
            @Override
            protected void onPostExecute(LocationWeatherInfo result) {
                if (result!=null) {
                    tvTemp.setText(String.valueOf((int) Math.round(result.todayWeatherInfo.temperature))+"°");
                    ivWeather.setImageDrawable(getResources().getDrawable(wda.getResources().getIdentifier("w" + result.todayWeatherInfo.image, "drawable", packagename)));
                }
            }
        }.execute(new QuerryLocation(location.getLongitude(), location.getLatitude()));
    }
}
