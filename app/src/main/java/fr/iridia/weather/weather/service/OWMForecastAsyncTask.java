package fr.iridia.weather.weather.service;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import fr.iridia.weather.weather.data.LocationWeatherInfo;
import fr.iridia.weather.weather.data.QuerryLocation;
import fr.iridia.weather.weather.data.WeatherInfo;

public class OWMForecastAsyncTask extends AsyncTask<QuerryLocation, Integer, LocationWeatherInfo > {

    public static final String TAG = "OWMForecastAsyncTask";

    protected int days = 3;

    public OWMForecastAsyncTask() {
        super();
    }

    public OWMForecastAsyncTask(int days) {
        super();
        this.days = days;
    }

    @Override
    protected LocationWeatherInfo doInBackground(QuerryLocation... loc) {
        LocationWeatherInfo ret = new LocationWeatherInfo();

        String querry = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%LAT%&lon=%LON%&cnt=%DAYS%&mode=json";
        querry = querry
                .replace("%LAT%", Double.toString(loc[0].latitude))
                .replace("%LON%", Double.toString(loc[0].longitude))
                .replace("%DAYS%", Integer.toString(days));

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(querry);
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = "";
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            return null;
        }
        finally {
            try{
                if(inputStream != null)
                    inputStream.close();
            } catch (Exception squish) {}
        }

        if (!result.isEmpty()) {
            try {
                JSONObject jObject = new JSONObject(result);

                JSONArray list = jObject.getJSONArray("list");

                for (int i = 0; i < list.length(); i++) {
                    String weatherIcon = "w" + list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                    //Log.i(TAG, "FORECAST PART " + (i+1) + " of " + list.length() + " : " + weatherIcon);
                    ret.Forecast.add(new WeatherInfo(weatherIcon));
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        } else {
            return null;
        }

        return ret;
    }

    @Override
    protected void onPostExecute(LocationWeatherInfo result) {
        super.onPostExecute(result);
    }
}
