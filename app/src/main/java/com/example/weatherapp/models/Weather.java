package com.example.weatherapp.models;

public class Weather {
    private String city;
    private String date;
    private String time;
    private String temperature;
    private String humidity;
    private String pressure;
    private String rain;


    public Weather(){

    }
    public Weather( String city, String date, String time, String temperature, String humidity, String pressure, String rain) {
        this.city = city;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.rain = rain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pressure='" + pressure + '\'' +
                ", rain='" + rain + '\'' +
                '}';
    }
}
