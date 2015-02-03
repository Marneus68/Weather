package fr.iridia.weather.weather;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.iridia.weather.weather.data.LocationWeatherInfo;

public class ArrayAdapterFavCity extends ArrayAdapter {

    public static final String TAG = "ArrayAdapterFavCity";

    Context             context;
    int                 resId;

    public ArrayAdapterFavCity(Context context, int resource, ArrayList<LocationWeatherInfo> data) {
        super(context, resource, data);

        this.context = context;
        this.resId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView =  (RelativeLayout) ((Activity) context).getLayoutInflater().inflate(resId, parent, false);
        }

        LocationWeatherInfo item = (LocationWeatherInfo) getItem(position);

        TextView cityNameTextView = (TextView) convertView.findViewById(R.id.cityNameTextView);
        cityNameTextView.setText(item.name);

        /*
        new OpenWeatherMapGPSAsyncTask(cityWeatherImageView) {
            @Override
            protected void onPostExecute(LocationWeatherInfo result) {
                ((ImageView) baseView).setImageDrawable(wda.getResources().getDrawable(wda.getResources().getIdentifier("w" + result.todayWeatherInfo.image, "drawable", packagename)));

                final LocationWeatherInfo fresult = result;
                if (result!=null) {
                    final Animation animationAppear = AnimationUtils.loadAnimation(wda, R.anim.appearing);
                    animationAppear.setDuration(200);
                    animationAppear.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation paramAnimation) {
                            cityWeatherImageView.setImageDrawable(wda.getResources().getDrawable(wda.getResources().getIdentifier("w" + fresult.todayWeatherInfo.image, "drawable", packagename)));
                        }
                        public void onAnimationRepeat(Animation paramAnimation) {}
                        public void onAnimationEnd(Animation paramAnimation) {}
                    });
                    cityWeatherImageView.startAnimation(animationAppear);
                }
            }
        }.execute(new QuerryLocation(item.longitude, item.latitude));
        */

        return convertView;
    }
}
