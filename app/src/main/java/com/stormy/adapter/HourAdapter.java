package com.stormy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stormy.R;
import com.stormy.weather.HourWeather;

/**
 * Created by mauro on 28/09/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private HourWeather[] hourWeathers;
    private Context context;


    public HourAdapter(Context context, HourWeather[] hourWeathers){
        this.context = context;
        this.hourWeathers = hourWeathers;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hourly_list_item, viewGroup, false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
        hourViewHolder.bindHour(hourWeathers[i]);
    }

    @Override
    public int getItemCount() {
        return hourWeathers.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView timeLabel;
            public TextView summaryLabel;
            public TextView temperatureLabel;
            public ImageView iconImageView;



        public HourViewHolder(View itemView) {
            super(itemView);

            timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            summaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            temperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            itemView.setOnClickListener(this);
        }

        public void bindHour(HourWeather hourWeather){
                timeLabel.setText(hourWeather.getHour());
                summaryLabel.setText(hourWeather.getSummary());
                temperatureLabel.setText(hourWeather.getTemperature() + "");
                iconImageView.setImageResource(hourWeather.getIconId());
        }

        @Override
        public void onClick(View view) {
            String message = timeLabel.getText().toString();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
