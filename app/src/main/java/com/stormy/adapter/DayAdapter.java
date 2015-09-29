package com.stormy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormy.R;
import com.stormy.weather.DailyWeather;

/**
 * Created by mauro on 25/09/15.
 */
public class DayAdapter extends BaseAdapter {

    private Context context;
    private DailyWeather[] dailyWeathers;

    public DayAdapter(Context context, DailyWeather[] dailyWeathers){
        this.context = context;
        this.dailyWeathers = dailyWeathers;
    }

    @Override
    public int getCount() {
        return dailyWeathers.length;
    }

    @Override
    public Object getItem(int i) {
        return dailyWeathers[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view  = LayoutInflater.from(context).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) view.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) view.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) view.findViewById(R.id.dayNameLabel);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        DailyWeather  dailyWeather = dailyWeathers[i];
        if(i == 0){
            holder.dayLabel.setText("Hoje");
        }else {
            holder.dayLabel.setText(dailyWeather.getDaysOfTheWeek());
        }
        holder.temperatureLabel.setText(dailyWeather.getTemperatureMax() + "");
        holder.iconImageView.setImageResource(dailyWeather.getIconId());
        return view;
    }

    private static class ViewHolder{
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
