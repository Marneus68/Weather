package fr.iridia.weather.weather;

import android.os.AsyncTask;

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
import fr.iridia.weather.weather.data.QuerryGPSLocation;
import fr.iridia.weather.weather.data.WeatherInfo;

public class OpenWeatherMapGPSAsyncTask extends AsyncTask<QuerryGPSLocation, Integer, LocationWeatherInfo> {

    public static final String TAG = "OpenWeatherMapGPSAsyncTask";

    protected LocationWeatherInfo doInBackground(QuerryGPSLocation... loc) {

        String querry = "http://api.openweathermap.org/data/2.5/weather?lat=%LAT%&lon=%LON%";
        querry = querry.replace("%LAT%", Double.toString(loc[0].latitude)).replace("%LON%", Double.toString(loc[0].longitude));

        DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
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

        LocationWeatherInfo lwi = new LocationWeatherInfo();
        if (!result.isEmpty()) {
            try {
                JSONObject jObject = new JSONObject(result);
                lwi.name = jObject.getString("name");
                lwi.longitude = loc[0].longitude;
                lwi.latitude = loc[0].latitude;
                JSONArray jweather = jObject.getJSONArray("weather");
                lwi.todayWeatherInfo = new WeatherInfo(jObject.getJSONObject("main").getDouble("temp")-273.15, jweather.getJSONObject(0).getString("icon"));
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
        return lwi;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(LocationWeatherInfo result) {

    }
}
