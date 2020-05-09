package com.cm.hw.weather.network;

import java.util.HashMap;

import com.cm.hw.weather.datamodel.City;

public interface  CityResultsObserver {
    public void receiveCitiesList(HashMap<String, City> citiesCollection);
    public void onFailure(Throwable cause);
}
