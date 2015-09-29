package com.stormy.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mauro on 21/09/15.
 */
public class CurrentWeather {
    private String icon;
    private Long time;
    private double temperature;
    private double humidity;
    private double precipitation;
    private String summary;
    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public CurrentWeather(String icon, Long time, double temperature, double humidity, double precipitation, String summary, String timeZone) {
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date date = new Date(getTime() * 1000);
        String timeString = formatter.format(date);
        return timeString;
    }


    public int getIconId(){
        return Forecast.getIcon(icon);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getTemperature() {
        double celsius = (temperature - 32) * 5.0 / 9.0;
        return (int)Math.round(celsius);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        double hum = humidity * 100;
        int i = (int) Math.round(hum) ;
        return i;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getPrecipitation() {
        double percent = precipitation * 100;
        return (int)Math.round(percent);
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
