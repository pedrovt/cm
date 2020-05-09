package com.cm.hw.weather.ui;

import com.cm.hw.weather.R;

/**
 * 2_IPMAWeather - WeatherTypeIcons <br>
 *
 * @author Pedro Teixeira pedro.teix@ua.pt
 * @version 1.0 - May 09, 2020
 */
public class WeatherTypeIcons {

    public static int getIcon(int idWeatherType) {
        switch (idWeatherType) {
            case 1:
                return R.drawable.weather_state_day_clear;
                
            case 2:
                return R.drawable.weather_state_day_partial_cloud;
                
            case 3:
                return R.drawable.weather_state_day_partial_cloud;
                
            case 4:
                return R.drawable.weather_state_cloudy;
                
            case 5:
                return R.drawable.weather_state_cloudy;
                
            case 6:
                return R.drawable.weather_state_rain;
                
            case 7:
                return R.drawable.weather_state_partial_rain;
                
            case 8:
                return R.drawable.weather_state_rain;
                
            case 9:
                return R.drawable.weather_state_rain;
            
            case 10:
                return R.drawable.weather_state_partial_rain;
            
            case 11:
                return R.drawable.weather_state_rain;
            
            case 12:
                return R.drawable.weather_state_partial_rain;
            
            case 13:
                return R.drawable.weather_state_partial_rain;
                
            case 14:
                return R.drawable.weather_state_rain;
                
            case 15:
                return R.drawable.weather_state_partial_rain;
                
            case 16:
                return R.drawable.weather_state_mist;
                
            case 17:
                return R.drawable.weather_state_fog;
                
            case 18:
                return R.drawable.weather_state_snow;
                
            case 19:
                return R.drawable.weather_state_thunder;
                
            case 20:
                return R.drawable.weather_state_rain_thunder;
                
            case 21:
                return R.drawable.weather_state_snow;
                
            case 22:
                return R.drawable.weather_state_snow;
                
            case 23:
                return R.drawable.weather_state_rain_thunder;
                
            case 24:
                return R.drawable.weather_state_overcast;
                
            case 25:
                return R.drawable.weather_state_day_partial_cloud;
                
            case 26:
                return R.drawable.weather_state_fog;
                
            case 27:
                return R.drawable.weather_state_cloudy;
                
            default:
                return R.drawable.weather_state_day_clear;
                
        }

    }
}
