package com.aren.ArenDemoBot.rest;

import com.aren.ArenDemoBot.config.BotConfig;
import com.aren.ArenDemoBot.model.Currency;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyRestMap {

    private static RestTemplate restTemplate;
    private final BotConfig config;

    public CurrencyRestMap(RestTemplate restTemplate, BotConfig config) {
        CurrencyRestMap.restTemplate = restTemplate;
        this.config = config;
    }

    /**
     * Get currency rate for given from and to currencies
     *
     * @param from Currency from which the rate is required
     * @param to Currency to which the rate is required
     * @return Currency object containing rate
     */
    public Currency getCurrencyRate(String from, String to) {
        setConverters();
        try {
            // Replace placeholders in API URL with actual values
            ResponseEntity<Currency> responseEntity = restTemplate.getForEntity(config.getCurrencyRateApi()
                    .replace("{from}", from)
                    .replace("{to}", to), Currency.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get currency rate for given list of from currencies and to currency
     *
     * @param froms List of currencies from which the rate is required
     * @param to Currency to which the rate is required
     * @return Currency object containing rate
     */
    public Currency getCurrencyRates(List<String> froms, String to) {
        setConverters();
        try {
            // Join froms and to currencies to form a comma separated string to replace placeholders in API URL
            String replace = froms.stream().map(it -> it.concat(to)).collect(Collectors.joining(","));
            //replace placeholders in API URL
            return restTemplate.getForEntity(config.getCurrencyRateApi()
                    .replace("{from}{to}",  replace), Currency.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set converters for RestTemplate
     */
    private static void setConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }
}