package com.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mauro on 25/09/15.
 */
public class HourWeather implements Parcelable{
    private long time;
    private String summary;
    private double temperature;
    private String icon;
    private String timezone;

    public HourWeather(){

    }

    public String getHour(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    public int getIconId(){
        return Forecast.getIcon(icon);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTemperature() {
        double celsius = (temperature - 32) * 5.0 / 9.0;
        return (int)Math.round(celsius);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(temperature);
        dest.writeString(icon);
        dest.writeString(timezone);

    }

    private HourWeather(Parcel in){
        time = in.readLong();
        summary = in.readString();
        temperature = in.readDouble();
        icon = in.readString();
        timezone = in.readString();
    }

    public static final Creator<HourWeather> CREATOR = new Creator<HourWeather>() {
        @Override
        public HourWeather createFromParcel(Parcel parcel) {
            return new HourWeather(parcel);
        }

        @Override
        public HourWeather[] newArray(int i) {
            return new HourWeather[i];
        }
    };
}
