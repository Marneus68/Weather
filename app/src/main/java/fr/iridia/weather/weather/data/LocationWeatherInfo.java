package fr.iridia.weather.weather.data;

import android.location.Location;

import java.util.ArrayList;

public class LocationWeatherInfo {
    public String name;
    public double longitude;
    public double latitude;
    public WeatherInfo todayWeatherInfo;

    public ArrayList<WeatherInfo> Forecast;

    @Override
    public String toString() {
        return name + " " + longitude + ":" + latitude;
    }

    public LocationWeatherInfo() {
        Forecast = new ArrayList<>();
    }

    public LocationWeatherInfo(String name, double lon, double lat) {
        this.name       = name;
        this.longitude  = lon;
        this.latitude   = lat;
    }
}