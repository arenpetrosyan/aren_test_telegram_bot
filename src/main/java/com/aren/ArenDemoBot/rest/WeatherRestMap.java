package com.aren.ArenDemoBot.rest;

import com.aren.ArenDemoBot.config.BotConfig;
import com.aren.ArenDemoBot.model.WeatherNow;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class WeatherRestMap {

    private final RestTemplate restTemplate;
    private final BotConfig config;

    public WeatherRestMap(RestTemplate restTemplate, BotConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    /**
     * Method to get the current weather for a given city
     *
     * @param city The city to get the weather for
     * @return The current weather of the city
     */
    public WeatherNow getNowWeather(String city){
        // Replace placeholders in API URL with actual values
        try {
            return restTemplate.getForObject(config.getNowWeatherApi()
                            .replace("{city}", city),
                    WeatherNow.class);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to check if a given text is a valid city name
     *
     * @param city The text to check
     * @return True if the text is a valid city name, false otherwise
     */
    @SneakyThrows
    public boolean isCity(String city) {
        URL weatherApiUrl = new URL(config.getNowWeatherApi().replace("{city}",city));
        HttpURLConnection weatherApiConnection = (HttpURLConnection)weatherApiUrl.openConnection();
        weatherApiConnection.setRequestMethod("GET");
        weatherApiConnection.connect();
        return weatherApiConnection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}