package pt.ua.nextweather.ui;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pt.ua.nextweather.R;
import pt.ua.nextweather.datamodel.City;
import pt.ua.nextweather.datamodel.Weather;
import pt.ua.nextweather.datamodel.WeatherType;
import pt.ua.nextweather.network.CityResultsObserver;
import pt.ua.nextweather.network.ForecastForACityResultsObserver;
import pt.ua.nextweather.network.IpmaWeatherClient;
import pt.ua.nextweather.network.WeatherTypesResultsObserver;

public class MainActivity extends AppCompatActivity {

    private TextView feedback;

    IpmaWeatherClient client = new IpmaWeatherClient();
    private HashMap<String, City> cities;
    private HashMap<Integer, WeatherType> weatherDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        feedback = findViewById(R.id.tvFeedback);

        getWeatherDescriptions();
        getListOfCities();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecastFromCity("Aveiro");
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getWeatherDescriptions() {

        feedback.append("\nGetting weather descriptions\n");

        // call the remote api, passing an (anonymous) listener to get back the results
        client.retrieveWeatherConditionsDescriptions(new WeatherTypesResultsObserver() {
            @Override
            public void receiveWeatherTypesList(HashMap<Integer, WeatherType> descriptorsCollection) {
                MainActivity.this.weatherDescriptions = descriptorsCollection;
                Log.d("callWeatherStep1", MainActivity.this.weatherDescriptions.toString());
                getListOfCities();
            }
            @Override
            public void onFailure(Throwable cause) {
                feedback.append("Failed to get weather conditions!" + cause.getMessage());
            }
        });


    }

    private void getListOfCities() {

        feedback.append("\nGetting list of cities"); feedback.append("\n");
        
        client.retrieveCitiesList(new CityResultsObserver() {

            @Override
            public void receiveCitiesList(HashMap<String, City> citiesCollection) {
                MainActivity.this.cities = citiesCollection;
                feedback.append("\nWeather descriptions: " + weatherDescriptions + "\n");
                feedback.append("\nList of cities: " + cities + "\n");
                // TODO update recycler view
            }

            @Override
            public void onFailure(Throwable cause) {
                feedback.append("Failed to get cities list!");
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
                        feedback.append(day.toString());
                        feedback.append("\t");
                    }
                    // TODO update fragment
                }
                @Override
                public void onFailure(Throwable cause) {
                    feedback.append( "Failed to get forecast for 5 days");
                }
            });
        } else {
            feedback.append("unknown city: " + city);
        }
    }

}
