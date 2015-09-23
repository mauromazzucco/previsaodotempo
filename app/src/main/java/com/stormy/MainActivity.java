package com.stormy;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;

    @Bind(R.id.timeLabel) TextView timeLabel;
    @Bind(R.id.temperatureLabel) TextView temperatureLabel;
    @Bind(R.id.humidityValue) TextView humidityValue;
    @Bind(R.id.precipValue) TextView precipValue;
    @Bind(R.id.summaryLabel) TextView summaryLabel;
    @Bind(R.id.iconImageView) ImageView iconImageView;
    @Bind(R.id.refreshImageView) ImageView refreshImageView;
    @Bind(R.id.progressBar) ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.INVISIBLE);

        final double latitude =  -23.3004;
        final double longitude = -47.2757;

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(latitude,longitude);

            }
        });
        getForecast(latitude,longitude);
    }

    private void getForecast(double latitude, double longitude) {
        String api = "ed218614c63874ef6ba4b3db0e610f1b";
        String forecastUrl = "https://api.forecast.io/forecast/" + api + "/" + latitude + "," + longitude;
        if(isNetWorkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUser();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String json = response.body().string();
                        Log.v(TAG, json);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(json);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUser();
                        }
                    } catch (IOException e) {
                        Log.i(TAG, "Caught", e);
                    }
                      catch (JSONException e){
                        Log.i(TAG, "Caught", e);
                    }
                }


            });

        }else{
            Toast.makeText(this, R.string.network_not_available, Toast.LENGTH_LONG).show();

        }
    }

    private void toggleRefresh() {
        if(progressBar.getVisibility() == View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay() {
        temperatureLabel.setText(currentWeather.getTemperature() + "");
        timeLabel.setText("At " + currentWeather.getFormattedTime() + " it will be");
        humidityValue.setText(currentWeather.getHumidity() + "%");
        precipValue.setText(currentWeather.getPrecipitation() + "%");
        summaryLabel.setText(currentWeather.getSummary());
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());
        iconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String json) throws JSONException {
        JSONObject forecast = new JSONObject(json);
        String timezone = forecast.getString("timezone");
        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather(
                currently.getString("icon"), currently.getLong("time"), currently.getDouble("temperature"),
                currently.getDouble("humidity"), currently.getDouble("precipProbability"),
                currently.getString("summary"), timezone);
        Log.i(TAG, "TIME: " + currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetWorkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean net = false;
        if(networkInfo != null && networkInfo.isConnected()){
            net = true;

        }

        return net;
    }

    private void alertUser() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
