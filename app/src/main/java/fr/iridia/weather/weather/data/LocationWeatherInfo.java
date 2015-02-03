package fr.iridia.weather.weather.data;

import android.location.Location;

public class LocationWeatherInfo {
    public String name;
    public double longitude;
    public double latitude;
    public WeatherInfo todayWeatherInfo;
    public WeatherInfo tomorowWeatherInfo;
    public WeatherInfo nextWeekWeatherInfo;

    @Override
    public String toString() {
        return name + " " + longitude + ":" + latitude;
    }

    public LocationWeatherInfo() {}

    public LocationWeatherInfo(String name, double lon, double lat) {
        this.name       = name;
        this.longitude  = lon;
        this.latitude   = lat;
    }
}