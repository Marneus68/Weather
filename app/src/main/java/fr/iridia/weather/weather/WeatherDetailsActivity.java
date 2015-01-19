package fr.iridia.weather.weather;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import static fr.iridia.weather.weather.WeatherDetailsActivity.DETAIL_ACTIVITY_EXTRA_KEY;

public class WeatherDetailsActivity extends ActionBarActivity {

    public static final String TAG = "ActionBarActivity";

    public static final String  DETAIL_ACTIVITY_KEY = "EXTRA_DETAIL_ACTIVITY_TYPE";
    public static final String  DETAIL_ACTIVITY_EXTRA_KEY = "EXTRA_DETAIL_ACTIVITY_EXTRA_KEY";
    public static final class   DETAIL_ACTIVITY_TYPES {
        public static final String HERE                     = "here";
        public static final String NAMED_CITY               = "named_city";
        public static final String NAMED_GPS_COORDINATES    = "named_gps_coordinates";

        private  DETAIL_ACTIVITY_TYPES() {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        switch (getIntent().getStringExtra(DETAIL_ACTIVITY_KEY)) {
            case DETAIL_ACTIVITY_TYPES.HERE:
                TextView tv = (TextView) findViewById(R.id.detailsTitle);
                tv.setText(R.string.here);
                break;
            case DETAIL_ACTIVITY_TYPES.NAMED_CITY:
                break;
            case DETAIL_ACTIVITY_TYPES.NAMED_GPS_COORDINATES:
                break;
        }
    }
}
