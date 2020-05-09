package com.cm.hw.weather.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cm.hw.weather.R;
import com.cm.hw.weather.datamodel.Weather;
import com.cm.hw.weather.datamodel.WeatherType;
import com.cm.hw.weather.datamodel.WindSpeed;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class WeatherForecastFragment extends Fragment {

    private static final String FORECAST_KEY = "WeatherForecastFragment.forecastList";

    private Weather[] mForecast;

    public WeatherForecastFragment() {
    }

    /**
     * This method loads the content specified by the fragment arguments.
     *
     * @param savedInstanceState Bundle with recent saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(FORECAST_KEY)) {
            // Load the content specified by the fragment arguments.

            // convert json to list
            Gson gson = new Gson();
            String forecastJsonList = getArguments().getString(FORECAST_KEY);
            mForecast = gson.fromJson(forecastJsonList, Weather[].class);
        }
    }

    /**
     * This method inflates the fragment's view and shows the forecast
     * detail information.
     *
     * @param inflater LayoutInflater object to inflate views
     * @param container ViewGroup that the fragment's UI should be attached to
     * @param savedInstanceState Bundle containing previous state
     * @return Fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_forecast_fragment,
                container, false);

        // Show the detail information in a TextView.
        if (mForecast != null) {
            for (int i = 0; i < mForecast.length; i++) {
                Log.i("Forecast Info for day " + (i + 1), mForecast[i].toString());
            }
            // TODO other days
            Weather forecast = mForecast[0];

            // City Name
            ((TextView) rootView.findViewById(R.id.city_name)).setText(forecast.getCityName());

            // Forecast Date
            ((TextView) rootView.findViewById(R.id.forecast_date)).setText(forecast.getForecastDate());

            // Weather Type (icon + string)
            // TODO icon
            WeatherType weatherType = MainActivity.weatherDescriptions.get(forecast.getIdWeatherType());
            ((TextView) rootView.findViewById(R.id.status)).setText(weatherType.getDescIdWeatherTypeEN());

            // Minimum Temperature
            ((TextView) rootView.findViewById(R.id.min_temp)).setText(String.format("%s ºC", forecast.getTMin()));

            // Maximum Temperature
            ((TextView) rootView.findViewById(R.id.max_temp)).setText(String.format("%s ºC", forecast.getTMax()));

            // Wind (direction + speed)
            String windSpeed = MainActivity.windDescriptions.get(forecast.getClassWindSpeed());
            ((TextView) rootView.findViewById(R.id.wind)).setText(String.format("%s - %s", windSpeed, forecast.getPredWindDir()));

            // Rain Probability
            ((TextView) rootView.findViewById(R.id.rain_probability)).setText(String.format("%s %%", forecast.getPrecipitaProb()));

        }

        return rootView;
    }

    /**
     * This method sets up a bundle for the arguments to pass
     * to a new instance of this fragment.
     *
     * @param forecast Forecast for the next n days
     * @return fragment
     */
    public static WeatherForecastFragment newInstance (List<Weather> forecast) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        // Set the bundle arguments for the fragment.
        Bundle arguments = new Bundle();

        // convert list to json
        Gson gson = new Gson();
        String forecastJsonList = gson.toJson(forecast);

        arguments.putString(FORECAST_KEY, forecastJsonList);
        fragment.setArguments(arguments);
        return fragment;
    }
}
