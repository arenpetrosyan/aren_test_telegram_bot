package com.aren.ArenDemoBot.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aren.ArenDemoBot.model.MainWeather;
import com.aren.ArenDemoBot.model.WeatherNow;
import com.aren.ArenDemoBot.rest.WeatherRestMap;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {WeatherService.class})
@ExtendWith(SpringExtension.class)
class WeatherServiceTest {
    @MockBean
    private WeatherRestMap weatherRestMap;

    @Autowired
    private WeatherService weatherService;

    /**
     * Method under test: {@link WeatherService#isCity(String)}
     */
    @Test
    void testIsCityTrue() {
        when(weatherRestMap.isCity(anyString())).thenReturn(true);

        assertTrue(weatherService.isCity("Moscow"));
        verify(weatherRestMap).isCity(anyString());
    }

    /**
     * Method under test: {@link WeatherService#isCity(String)}
     */
    @Test
    void testIsCityFalse() {
        when(weatherRestMap.isCity(anyString())).thenReturn(false);

        assertFalse(weatherService.isCity("False City"));
        verify(weatherRestMap).isCity(anyString());
    }

    /**
     * Method under test: {@link WeatherService#getCurrentWeather(String)}
     */
    @Test
    void testGetCurrentWeather() {
        MainWeather mainWeather = new MainWeather();
        mainWeather.setFeelsLike(15);
        mainWeather.setTemp(18);

        WeatherNow weatherNow = new WeatherNow();
        weatherNow.setMain(mainWeather);
        weatherNow.setWeather(new ArrayList<>());
        when(weatherRestMap.getNowWeather(anyString())).thenReturn(weatherNow);

        assertSame(weatherNow, this.weatherService.getCurrentWeather("Moscow"));
        verify(weatherRestMap).getNowWeather(anyString());
    }
}

