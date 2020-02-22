package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class WeatherActivity extends AppCompatActivity {
    private String city;
    private TextView showCity, humidity, pressure, temp, tempmin, tempmax;
    private ImageView icon;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        final Intent intent = getIntent();
        city = intent.getStringExtra("CITY");

        showCity = findViewById(R.id.city);
        icon = findViewById(R.id.imageicon);
        temp = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        tempmin = findViewById(R.id.tempmin);
        tempmax = findViewById(R.id.tempmax);

        getWeather();

    }

    private void getWeather(){

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        JsonWeatherAPI jsonweatherapi = retrofit.create(JsonWeatherAPI.class);
        Call call = jsonweatherapi.getWeather(city, "749561a315b14523a8f5f1ef95e45864", "metric");

        call.enqueue(new Callback<WResponse>() {
            @Override
            public void onResponse(Call<WResponse> call, Response<WResponse> response) {
                if (response.body() != null) {
                    WResponse wResponse = response.body();
                    saveCity(wResponse.getName());
                    showCity.setText(wResponse.getName());
                    final String imgURL  = "https://openweathermap.org/img/w/"+wResponse.getWeather().get(0).getIcon()+".png";
                    new DownloadImageTask(icon).execute(imgURL);
                    temp.setText(wResponse.getMain().getTemp() + " °C");
                    humidity.setText(wResponse.getMain().getHumidity()+" %");
                    pressure.setText(wResponse.getMain().getPressure()+" hPa");
                    tempmin.setText(wResponse.getMain().getTempMin()+" °C");
                    tempmax.setText(wResponse.getMain().getTempMax()+" °C");

                } else {
                    back();
                }
            }

            @Override
            public void onFailure(Call<WResponse> call, Throwable t) {
                showCity.setText(t.getMessage());
            }
        });

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    getWeather();
                }
                pullToRefresh.setRefreshing(false);
            }
        });

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (isNetworkAvailable()) {
                    getWeather();
                }
            }
        };

        timer.schedule(task,3000);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void back() {
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
        Toast.makeText(getApplicationContext(), "Podano nieprawidłową nazwę miasta!",
                Toast.LENGTH_SHORT).show();
    }

    private void saveCity(String name){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAME_KEY", name);
        editor.apply();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
