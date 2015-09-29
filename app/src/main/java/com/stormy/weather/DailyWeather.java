package com.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mauro on 25/09/15.
 */
public class DailyWeather implements Parcelable{
    private long time;
    private String summary;
    private double temperatureMax;
    private String icon;
    private String timezone;

    public DailyWeather(){

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

    public int getTemperatureMax() {
        double celsius = (temperatureMax - 32) * 5.0 / 9.0;
        return (int)Math.round(celsius);
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
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

    public int getIconId(){
        return Forecast.getIcon(icon);
    }

    public String getDaysOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date date = new Date(getTime() * 1000);
        return formatter.format(date);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(temperatureMax);
        dest.writeString(icon);
        dest.writeString(timezone);

    }

    private DailyWeather(Parcel in){
        time = in.readLong();
        summary = in.readString();
        temperatureMax = in.readDouble();
        icon = in.readString();
        timezone = in.readString();
    }

    public static final Creator<DailyWeather> CREATOR = new Creator<DailyWeather>() {
        @Override
        public DailyWeather createFromParcel(Parcel parcel) {
            return new DailyWeather(parcel);
        }

        @Override
        public DailyWeather[] newArray(int i) {
            return new DailyWeather[i];
        }
    };


}
