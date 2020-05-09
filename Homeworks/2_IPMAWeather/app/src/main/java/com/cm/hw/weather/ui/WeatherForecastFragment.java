package com.cm.hw.weather.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cm.hw.weather.R;
import com.cm.hw.weather.datamodel.Weather;
import com.cm.hw.weather.datamodel.WeatherType;
import com.google.gson.Gson;

import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_forecast_fragment,
                container, false);

        // Show the detail information in a Recycler Viw.
        if (mForecast != null) {
            for (int i = 0; i < mForecast.length; i++) {
                Log.i("Forecast Info for day " + (i + 1), mForecast[i].toString());
            }
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.weather_forecast_details_container);
        recyclerView.setAdapter(new ForecastRecyclerViewAdapter(mForecast));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
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

    /**
     * The RecyclerView for the forecast
     */
    class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {
        private final Weather[] mValues;

        ForecastRecyclerViewAdapter(Weather[] items) {
            mValues = items;
        }

        /**
         * This method inflates the layout for the cities list.
         * @param parent ViewGroup into which the new view will be added.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder
         */
        @Override
        public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_forecast, parent, false);
            return new ForecastViewHolder(view);
        }

        /**
         * @param holder   ViewHolder
         * @param position Position of the city in the array.
         */
        @Override
        public void onBindViewHolder(final ForecastViewHolder holder, int position) {
            Weather forecast = mValues[position];

            // City Name
            holder.mCityName.setText(forecast.getCityName());

            // Forecast Date
            holder.mForecastDate.setText(forecast.getForecastDate());

            // Weather Type (icon + string)
            WeatherType weatherType = MainActivity.weatherDescriptions.get(forecast.getIdWeatherType());
            holder.mStatus.setText(weatherType.getDescIdWeatherTypeEN());
            int icon = WeatherTypeIcons.getIcon(weatherType.getIdWeatherType());
            holder.mStatusIcon.setImageResource(icon);

            // Minimum Temperature
            holder.mMinTemp.setText(String.format("%s ºC", forecast.getTMin()));

            // Maximum Temperature
            holder.mMaxTemp.setText(String.format("%s ºC", forecast.getTMax()));

            // Wind (direction + speed)
            String windSpeed = MainActivity.windDescriptions.get(forecast.getClassWindSpeed());
            holder.mWind.setText(String.format("%s - %s", windSpeed, forecast.getPredWindDir()));

            // Rain Probability
            holder.mRainProb.setText(String.format("%s %%", forecast.getPrecipitaProb()));
        }

        /**
         * Get the count of forecast items.
         * @return Integer count
         */
        @Override
        public int getItemCount() {
            return mValues.length;
        }

        /**
         * ViewHolder describes an item view and metadata about its place
         * within the RecyclerView.
         */
        class ForecastViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mCityName, mForecastDate, mStatus, mMinTemp, mMaxTemp, mWind, mRainProb;
            final ImageView mStatusIcon;

            ForecastViewHolder(View view) {
                super(view);
                mView = view;
                mCityName     = (TextView)  view.findViewById(R.id.city_name);
                mForecastDate = (TextView)  view.findViewById(R.id.forecast_date);
                mStatus       = (TextView)  view.findViewById(R.id.status);
                mStatusIcon   = (ImageView) view.findViewById(R.id.status_icon);
                mMinTemp      = (TextView)  view.findViewById(R.id.min_temp);
                mMaxTemp      = (TextView)  view.findViewById(R.id.max_temp);
                mWind         = (TextView)  view.findViewById(R.id.wind);
                mRainProb     = (TextView)  view.findViewById(R.id.rain_probability);
            }
        }
    }
}
