package com.cm.hw.weather.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class WindSpeed {

    @Expose
    @SerializedName( "descClassWindSpeedDailyEN")
    private String descClassWindSpeedDailyEN;

    @Expose
    @SerializedName( "descClassWindSpeedDailyPT")
    private String descClassWindSpeedDailyPT;

    @Expose
    @SerializedName( "descIdWeatherTypeEN")
    private String descIdWeatherTypeEN;

    public WindSpeed(String descClassWindSpeedDailyEN, String descClassWindSpeedDailyPT, String descIdWeatherTypeEN) {
        this.descClassWindSpeedDailyEN = descClassWindSpeedDailyEN;
        this.descClassWindSpeedDailyPT = descClassWindSpeedDailyPT;
        this.descIdWeatherTypeEN = descIdWeatherTypeEN;
    }

    public String getDescClassWindSpeedDailyEN() {
        return descClassWindSpeedDailyEN;
    }

    public void setDescClassWindSpeedDailyEN(String descClassWindSpeedDailyEN) {
        this.descClassWindSpeedDailyEN = descClassWindSpeedDailyEN;
    }

    public String getDescClassWindSpeedDailyPT() {
        return descClassWindSpeedDailyPT;
    }

    public void setDescClassWindSpeedDailyPT(String descClassWindSpeedDailyPT) {
        this.descClassWindSpeedDailyPT = descClassWindSpeedDailyPT;
    }

    public String getDescIdWeatherTypeEN() {
        return descIdWeatherTypeEN;
    }

    public void setDescIdWeatherTypeEN(String descIdWeatherTypeEN) {
        this.descIdWeatherTypeEN = descIdWeatherTypeEN;
    }

    @Override
    public String toString() {
        return "WindSpeed{" +
                "descClassWindSpeedDailyEN='" + descClassWindSpeedDailyEN + '\'' +
                ", descClassWindSpeedDailyPT='" + descClassWindSpeedDailyPT + '\'' +
                ", descIdWeatherTypeEN='" + descIdWeatherTypeEN + '\'' +
                '}';
    }
}