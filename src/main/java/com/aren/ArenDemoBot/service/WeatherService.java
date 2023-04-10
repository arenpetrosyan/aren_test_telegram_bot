package com.aren.ArenDemoBot.service;
import com.aren.ArenDemoBot.model.WeatherNow;
import com.aren.ArenDemoBot.rest.WeatherRestMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class that provides weather-related functionality
 */

@Service
@Slf4j
public class WeatherService {

    private final WeatherRestMap weatherRestMap;

    public WeatherService(WeatherRestMap weatherRestMap) {
        this.weatherRestMap = weatherRestMap;
    }

    /**
     * Method to check if a city exists
     *
     * @param city The city to check
     * @return True if the city exists, false otherwise
     */
    @SneakyThrows
    public boolean isCity(String city) {
        log.info("Checking if city exists: {}", city);
        boolean cityExists = weatherRestMap.isCity(city);
        log.info("City '{}' exists: {}", city, cityExists);
        return cityExists;
    }

    /**
     * Method to get the current weather of a city
     *
     * @param city The city to get the weather for
     * @return The current weather of the city
     */
    public WeatherNow getCurrentWeather(String city){
        log.info("Getting current weather for city: {}", city);
        WeatherNow currentWeather = weatherRestMap.getNowWeather(city);
        log.info("Current weather for city {}: {}", city, currentWeather);
        return currentWeather;
    }}