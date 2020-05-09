package com.cm.hw.weather.network;

import java.util.List;

import com.cm.hw.weather.datamodel.Weather;

public interface ForecastForACityResultsObserver {
    public void receiveForecastList(List<Weather> forecast);
    public void onFailure(Throwable cause);
}
