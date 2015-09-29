package com.stormy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.stormy.R;
import com.stormy.weather.CurrentWeather;
import com.stormy.weather.DailyWeather;
import com.stormy.weather.Forecast;
import com.stormy.weather.HourWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast forecast;

    @Bind(R.id.timeLabel) TextView timeLabel;
    @Bind(R.id.temperatureLabel) TextView temperatureLabel;
    @Bind(R.id.humidityValue) TextView humidityValue;
    @Bind(R.id.precipValue) TextView precipValue;
    @Bind(R.id.summaryLabel) TextView summaryLabel;
    @Bind(R.id.iconImageView) ImageView iconImageView;
    @Bind(R.id.refreshImageView) ImageView refreshImageView;
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.locationLabel) TextView locationLabel;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat;
    double lng;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.INVISIBLE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConnected(savedInstanceState);

            }
        });

    }

    private void getForecast() {

            if (isNetWorkAvailable()) {
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null)
                    locationLabel.setText(addresses.get(0).getLocality() + " - " + addresses.get(0).getAdminArea());
                String api = "ed218614c63874ef6ba4b3db0e610f1b";
                String forecastUrl = "https://api.forecast.io/forecast/" + api + "/" + lat + "," + lng + "?lang=pt";
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
                                forecast = parseForecastDetails(json);
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
                        } catch (JSONException e) {
                            Log.i(TAG, "Caught", e);
                        }
                    }


                });

            } else {
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
        CurrentWeather currentWeather = forecast.getCurrentWeather();
        temperatureLabel.setText(currentWeather.getTemperature() + "");
        timeLabel.setText("Previsão do tempo as " + currentWeather.getFormattedTime());
        humidityValue.setText(currentWeather.getHumidity() + "%");
        precipValue.setText(currentWeather.getPrecipitation() + "%");
        summaryLabel.setText(currentWeather.getSummary());
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());
        iconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String json) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrentWeather(getCurrentDetails(json));
        forecast.setHourWeathers(getHourlyDetails(json));
        forecast.setDailyWeathers(getDailyDetails(json));
        return forecast;
    }

    private DailyWeather[] getDailyDetails(String json) throws JSONException{
        JSONObject forecast = new JSONObject(json);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        DailyWeather[] dailyWeathers = new DailyWeather[data.length()];

        for(int i = 0; i < data.length(); i++ ){
            JSONObject jsonDaily = data.getJSONObject(i);
            DailyWeather dailyWeather = new DailyWeather();

            dailyWeather.setSummary(jsonDaily.getString("summary"));
            dailyWeather.setIcon(jsonDaily.getString("icon"));
            dailyWeather.setTemperatureMax(jsonDaily.getDouble("temperatureMax"));
            dailyWeather.setTime(jsonDaily.getLong("time"));
            dailyWeather.setTimezone(timezone);

            dailyWeathers[i] = dailyWeather;
        }

        return dailyWeathers;
    }

    private HourWeather[] getHourlyDetails(String json) throws JSONException{
        JSONObject forecast = new JSONObject(json);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");
        HourWeather[] hourWeathers = new HourWeather[data.length()];

        for(int i = 0; i < data.length(); i++ ){
            JSONObject jsonHour = data.getJSONObject(i);
            HourWeather hourWeather = new HourWeather();
            hourWeather.setSummary(jsonHour.getString("summary"));
            hourWeather.setIcon(jsonHour.getString("icon"));
            hourWeather.setTemperature(jsonHour.getDouble("temperature"));
            hourWeather.setTime(jsonHour.getLong("time"));
            hourWeather.setTimezone(timezone);
            hourWeathers[i] = hourWeather;
        }

        return hourWeathers;
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
        NetworkInfo mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean net = false;
        String network = mMobile.getSubtypeName();
        if((mWifi.isAvailable() && mWifi.isConnected()) || (mMobile.isConnected() && mMobile.isAvailable())){
            if(network.equals("GPRS")) {
                net = false;
            }else{
                net = true;
            }

        }

        return net;
    }

    private void alertUser() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location == null){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            lat = location.getLatitude();
            lng = location.getLongitude();
            getForecast();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            getForecast();
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra("daily", forecast.getDailyWeathers());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view){
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra("hourly", forecast.getHourWeathers());
        startActivity(intent);
    }
}
