package com.cm.hw.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cm.hw.weather.R;
import com.cm.hw.weather.datamodel.*;
import com.cm.hw.weather.network.CityResultsObserver;
import com.cm.hw.weather.network.ForecastForACityResultsObserver;
import com.cm.hw.weather.network.IpmaWeatherClient;
import com.cm.hw.weather.network.WeatherTypesResultsObserver;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private IpmaWeatherClient client = new IpmaWeatherClient();
    private Map<String, City> cities;
    private List<String> citiesList;
    public  static Map<Integer, WeatherType> weatherDescriptions;
    public  static Map<Integer, String> windDescriptions;

    private boolean mTwoPane = false;

    private RecyclerView recyclerView;
    private SimpleItemRecyclerViewAdapter adapter;

    static {
        windDescriptions = new HashMap<>();
        windDescriptions.put(-99, "Unknown");
        windDescriptions.put(1, "Weak");
        windDescriptions.put(2, "Moderate");
        windDescriptions.put(3, "Strong");
        windDescriptions.put(4, "Very strong");
    }

    /**
     * Sets up a city list as a RecyclerView, and determines
     * whether the screen is wide enough for two-pane mode.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Set the toolbar as the app bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setLogo(R.drawable.ic_action_logo);

        // Obtain list of cities
        citiesList = new ArrayList<>();
        getWeatherDescriptions();

        // Get the cities list as a RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.cities_list);
        adapter = new SimpleItemRecyclerViewAdapter(citiesList);
        recyclerView.setAdapter(adapter);

        // Is the container layout available? If so, set mTwoPane to true.
        if (findViewById(R.id.weather_forecast_container) != null) {
            mTwoPane = true;
        }
    }

    /**
     * Get weather descriptions
     */
    private void getWeatherDescriptions() {

        Log.d(LOG_TAG, "\nGetting weather descriptions\n");

        // call the remote api, passing an (anonymous) listener to get back the results
        client.retrieveWeatherConditionsDescriptions(new WeatherTypesResultsObserver() {
            @Override
            public void receiveWeatherTypesList(HashMap<Integer, WeatherType> descriptorsCollection) {
                MainActivity.this.weatherDescriptions = descriptorsCollection;
                Log.d(LOG_TAG, MainActivity.this.weatherDescriptions.toString());
                getListOfCities();
            }
            @Override
            public void onFailure(Throwable cause) {
                Log.e(LOG_TAG, "Failed to get weather conditions!" + cause.getMessage());
            }
        });


    }

    /**
     * Get list of cities
     */
    private void getListOfCities() {

        Log.d(LOG_TAG, "\nGetting list of cities"); Log.d(LOG_TAG, "\n");

        client.retrieveCitiesList(new CityResultsObserver() {

            @Override
            public void receiveCitiesList(HashMap<String, City> citiesCollection) {
                MainActivity.this.cities = citiesCollection;
                citiesList.clear();
                citiesList.addAll(citiesCollection.keySet());

                Log.d(LOG_TAG, "\nWeather descriptions: " + weatherDescriptions + "\n");
                Log.d(LOG_TAG, "\nList of cities: " + cities + "\n");
                // update recycler view
                adapter.notifyItemRangeChanged(0, citiesCollection.size() - 1 );

            }

            @Override
            public void onFailure(Throwable cause) {
                Log.e(LOG_TAG, "Failed to get cities list!");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Toast.makeText(MainActivity.this, "Select a city and slide left or right to see the forecast for the next 5 days", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The RecyclerView for the cities list.
     */
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<String> mValues;

        SimpleItemRecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        /**
         * This method inflates the layout for the cities list.
         * @param parent ViewGroup into which the new view will be added.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cities_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * This method implements a listener with setOnClickListener().
         * When the user taps a city name, the code checks if mTwoPane
         * is true, and if so uses a fragment to show the weather forecast for that cit.
         * If mTwoPane is not true, it starts WeatherForecastActivity
         * using an intent with the forecast for the selected city.
         *
         * @param holder   ViewHolder
         * @param position Position of the city in the array.
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get selected city
                    int selectedCity = holder.getAdapterPosition();
                    String cityName = citiesList.get(selectedCity);
                    City city = cities.get(cityName);

                    /* Get forecast */
                    if( null != city) {
                        int localId = city.getGlobalIdLocal();
                        client.retrieveForecastForCity(localId, new ForecastForACityResultsObserver() {
                            @Override
                            public void receiveForecastList(List<Weather> forecast) {
                                for (Weather day : forecast) {
                                    Log.d(LOG_TAG, day.toString());
                                    Log.d(LOG_TAG, "\t");
                                    day.setCityName(cityName);
                                }
                                if (mTwoPane) {
                                    // Create new instance of fragment and add it to
                                    // the activity using a fragment transaction.
                                    WeatherForecastFragment fragment =
                                            WeatherForecastFragment.newInstance(forecast);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.weather_forecast_container, fragment)
                                            .addToBackStack(null)
                                            .commit();
                                }
                                else {
                                    // Send an intent to the WeatherForecastActivity
                                    // with intent extra of the forecast for the selected city.
                                    Context context = v.getContext();
                                    Intent intent = new Intent(context,
                                            WeatherForecastActivity.class);
                                    Gson gson = new Gson();
                                    String forecastJsonList = gson.toJson(forecast);
                                    intent.putExtra(WeatherForecastActivity.FORECAST_KEY,
                                            forecastJsonList);
                                    context.startActivity(intent);
                                }
                            }
                            @Override
                            public void onFailure(Throwable cause) {
                                Log.d(LOG_TAG,  "Failed to get forecast for 5 days");
                            }
                        });
                    } else {
                        Log.e(LOG_TAG, "unknown city: " + city);
                    }

                }
            });
        }

        /**
         * Get the count of cities list items.
         * @return Integer count
         */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * ViewHolder describes an item view and metadata about its place
         * within the RecyclerView.
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mContentView;
            String mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

}
