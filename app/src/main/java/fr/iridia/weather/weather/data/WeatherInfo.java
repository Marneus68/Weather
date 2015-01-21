package fr.iridia.weather.weather.data;

public class WeatherInfo {
    public double temperature;
    public String image;

    public WeatherInfo(double temp, String img) {
        temperature = temp;
        image = img;
    }

    public WeatherInfo() {
        this(0, "");
    }
}

