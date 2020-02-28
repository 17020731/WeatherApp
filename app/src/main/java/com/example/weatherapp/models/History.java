package com.example.weatherapp.models;

public class History {
    private String name_city;
    private String date_time;
    private String img;
    private String description;
    private int temp;
    private int pressure;
    private int humidity;


    public History(){

    }

    public History(String name_city, String date_time, String img, String description, int temp, int pressure, int humidity) {

        this.name_city = name_city;
        this.date_time = date_time;
        this.img = img;
        this.description = description;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName_city() {
        return name_city;
    }

    public void setName_city(String name_city) {
        this.name_city = name_city;
    }



    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "History{" +
                "name_city='" + name_city + '\'' +
                ", date_time='" + date_time + '\'' +
                ", img='" + img + '\'' +
                ", description='" + description + '\'' +
                ", temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                '}';
    }


}
