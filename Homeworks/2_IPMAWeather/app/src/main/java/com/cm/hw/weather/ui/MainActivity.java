/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cm.hw.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cm.hw.weather.R;
import com.cm.hw.weather.datamodel.*;
import com.cm.hw.weather.network.CityResultsObserver;
import com.cm.hw.weather.network.ForecastForACityResultsObserver;
import com.cm.hw.weather.network.IpmaWeatherClient;
import com.cm.hw.weather.network.WeatherTypesResultsObserver;
import com.cm.hw.weather.ui.content.SongUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of song titles (items). When one is
 * touched in vertical orientation (single pane), an intent starts
 * {@link SongDetailActivity} representing song details. When one is
 * touched in horizontal orientation on a tablet screen (two panes),
 * the activity shows a fragment.
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    IpmaWeatherClient client = new IpmaWeatherClient();
    private HashMap<String, City> cities;
    private ArrayList<String> citiesList;
    private HashMap<Integer, WeatherType> weatherDescriptions;

    // Default layout is one pane, not two.
    private boolean mTwoPane = false;

    private RecyclerView recyclerView;
    private SimpleItemRecyclerViewAdapter adapter;


    /**
     * Sets up a song list as a RecyclerView, and determines
     * whether the screen is wide enough for two-pane mode.
     * The song_detail_container view for MainActivity will be
     * present only if the screen's width is 900dp or larger,
     * because it is defined only in the "song_list.xml (w900dp).xml"
     * layout, not in the default "song_list.xml" layout for smaller
     * screen sizes. If this view is present, then the activity
     * should be in two-pane mode.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        // Set the toolbar as the app bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        getWeatherDescriptions();

        // List of cities
        citiesList = new ArrayList<>();
        
        // Get the song list as a RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.song_list);
        adapter = new SimpleItemRecyclerViewAdapter(citiesList);
        recyclerView.setAdapter(adapter);

        // Is the container layout available? If so, set mTwoPane to true.
        if (findViewById(R.id.song_detail_container) != null) {
            mTwoPane = true;
        }


    }

    /**
     * The RecyclerView for the song list.
     */
    class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter
            <SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<String> mValues;

        SimpleItemRecyclerViewAdapter(List<String> items) {
            mValues = items;
        }

        /**
         * This method inflates the layout for the song list.
         * @param parent ViewGroup into which the new view will be added.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.song_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * This method implements a listener with setOnClickListener().
         * When the user taps a song title, the code checks if mTwoPane
         * is true, and if so uses a fragment to show the song detail.
         * If mTwoPane is not true, it starts SongDetailActivity
         * using an intent with extra data about which song title was selected.
         *
         * @param holder   ViewHolder
         * @param position Position of the song in the array.
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mContentView.setText(mValues.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        // Get selected song position in song list.
                        int selectedSong = holder.getAdapterPosition();
                        // Create new instance of fragment and add it to
                        // the activity using a fragment transaction.
                        SongDetailFragment fragment =
                                SongDetailFragment.newInstance(selectedSong);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.song_detail_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // Send an intent to the SongDetailActivity
                        // with intent extra of the selected song position.
                        Context context = v.getContext();
                        Intent intent = new Intent(context,
                                SongDetailActivity.class);
                        intent.putExtra(SongUtils.SONG_ID_KEY,
                                holder.getAdapterPosition());
                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         * Get the count of song list items.
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
            final TextView mIdView;
            final TextView mContentView;
            String mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

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
                Log.d(LOG_TAG, "Failed to get weather conditions!" + cause.getMessage());
            }
        });


    }

    private void getListOfCities() {

        Log.d(LOG_TAG, "\nGetting list of cities"); Log.d(LOG_TAG, "\n");

        client.retrieveCitiesList(new CityResultsObserver() {

            @Override
            public void receiveCitiesList(HashMap<String, City> citiesCollection) {
                MainActivity.this.cities = citiesCollection;
                citiesList.clear();
                citiesList.addAll(citiesCollection.keySet());
                Log.i(LOG_TAG, "\nWeather descriptions: " + weatherDescriptions + "\n");
                Log.i(LOG_TAG, "\nList of cities: " + cities + "\n");
                // update recycler view

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable cause) {
                Log.d(LOG_TAG, "Failed to get cities list!");
            }
        });
    }

    private void getForecastFromCity(String city) {
        City cityFound = cities.get(city);
        if( null != cityFound) {
            int localId = cityFound.getGlobalIdLocal();
            client.retrieveForecastForCity(localId, new ForecastForACityResultsObserver() {
                @Override
                public void receiveForecastList(List<Weather> forecast) {
                    for (Weather day : forecast) {
                        Log.d(LOG_TAG, day.toString());
                        Log.d(LOG_TAG, "\t");
                    }
                    // TODO update fragment
                }
                @Override
                public void onFailure(Throwable cause) {
                    Log.d(LOG_TAG,  "Failed to get forecast for 5 days");
                }
            });
        } else {
            Log.d(LOG_TAG, "unknown city: " + city);
        }
    }

}
