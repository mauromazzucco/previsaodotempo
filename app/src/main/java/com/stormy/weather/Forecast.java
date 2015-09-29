package com.stormy.weather;

import com.stormy.R;

/**
 * Created by mauro on 25/09/15.
 */
public class Forecast {
    private CurrentWeather currentWeather;
    private HourWeather[] hourWeathers;
    private DailyWeather[] dailyWeathers;


    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public DailyWeather[] getDailyWeathers() {
        return dailyWeathers;
    }

    public void setDailyWeathers(DailyWeather[] dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
    }

    public HourWeather[] getHourWeathers() {
        return hourWeathers;
    }

    public void setHourWeathers(HourWeather[] hourWeathers) {
        this.hourWeathers = hourWeathers;
    }


    public static int getIcon(String icon){
        int iconId = R.drawable.clear_day;

        if (icon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (icon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (icon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (icon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (icon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (icon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}
