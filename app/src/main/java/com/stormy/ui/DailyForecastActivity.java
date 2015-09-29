package com.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.stormy.R;
import com.stormy.adapter.DayAdapter;
import com.stormy.weather.DailyWeather;

import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {
    private DailyWeather[] dailyWeathers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra("daily");
        dailyWeathers = Arrays.copyOf(parcelables, parcelables.length, DailyWeather[].class);
        DayAdapter adapter = new DayAdapter(this, dailyWeathers);
        setListAdapter(adapter);
    }

}
