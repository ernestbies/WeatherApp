package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonWeatherAPI {

    @GET("/data/2.5/weather")
    Call < WResponse > getWeather(@Query("q") String city, @Query("appid") String apiKey, @Query("units") String units );
}
