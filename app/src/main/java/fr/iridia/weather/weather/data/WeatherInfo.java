package fr.iridia.weather.weather.data;

public class WeatherInfo {
    public double temperature = 0;
    public String image = "";

    public WeatherInfo(String img) {
        this(0, img);
    }

    public WeatherInfo(double temp, String img) {
        temperature = temp;
        image = img;
    }

    public WeatherInfo() {
        this(0, "");
    }
}

