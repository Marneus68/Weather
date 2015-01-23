package fr.iridia.weather.weather.data;

public class QuerryLocation {
    public String name;
    public double longitude;
    public double latitude;

    public QuerryLocation(double lon, double lat) {
        longitude = lon;
        latitude = lat;
    }

    public QuerryLocation(String n, double lon, double lat) {
        this(lon, lat);
        name = n;
    }
}
