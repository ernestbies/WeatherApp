package com.example.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WResponse {
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;
    @SerializedName("weather")
    @Expose
    private ArrayList<Weather> weather;

    public ArrayList<Weather> getWeather() {return weather; }

    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }
    public Main getMain() {
        return main;
    }
    public void setMain(Main main) {
        this.main = main;
    }
    public Integer getDt() {
        return dt;
    }
    public void setDt(Integer dt) {
        this.dt = dt;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getCod() {
        return cod;
    }
    public void setCod(Integer cod) {
        this.cod = cod;
    }

}
