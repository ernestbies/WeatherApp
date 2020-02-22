package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Normalizer;


public class MainActivity extends AppCompatActivity {
    private EditText city;
    private Button check;
    private TextView textInternet;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.city);
        check = findViewById(R.id.checkWeather);
        textInternet = findViewById(R.id.textInternet);

        loadCity();
        checkInternet();

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
             checkInternet();
             pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void checkInternet(){
        if (!isNetworkAvailable()) {
            city.setEnabled(false);
            check.setEnabled(false);
            textInternet.setVisibility(View.VISIBLE);

        } else {
            city.setEnabled(true);
            check.setEnabled(true);
            textInternet.setVisibility(View.INVISIBLE);
        }
    }

    public void sendCity(View view) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("CITY", normalizacjaZnakow(city.getText().toString()));
        startActivity(intent);

    }

    private String normalizacjaZnakow(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    private void loadCity(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String name = sharedPreferences.getString("NAME_KEY", "");
        city.setText(name);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
