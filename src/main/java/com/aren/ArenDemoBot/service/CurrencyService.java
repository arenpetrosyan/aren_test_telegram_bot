package com.aren.ArenDemoBot.service;

import com.aren.ArenDemoBot.enums.CurrencyCodes;
import com.aren.ArenDemoBot.model.Currency;
import com.aren.ArenDemoBot.rest.CurrencyRestMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class that provides currency-related functionality
 */

@Service
@Slf4j
public class CurrencyService {
    private final CurrencyRestMap currencyRestMap;

    public CurrencyService(CurrencyRestMap currencyRestMap) {
        this.currencyRestMap = currencyRestMap;
    }

    /**
     * Get the currency rate from one currency to another
     *
     * @param from The currency to convert from
     * @param to   The currency to convert to
     * @return The rate of conversion
     */
    public String getCurrencyRate(String from, String to) {
        log.info("Getting currency rate from {} to {}", from, to);
        Currency currency = currencyRestMap.getCurrencyRate(from, to);
        String rate = currency.getData().get(from + to);
        log.info("Conversion rate from {} to {} is {}", from, to, rate);
        return rate;
    }

    /**
     * Get the currency rates from a list of currencies to another
     *
     * @param froms The list of currencies to convert from
     * @param to    The currency to convert to
     * @return A map of the rates of conversion
     */
    public Map<String, String> getCurrencyRates(List<String> froms, String to) {
        log.info("Getting currency rates from {} to multiple currencies", to);
        Currency currencies = currencyRestMap.getCurrencyRates(froms, to);
        Map<String, String> result = new HashMap<>();
        Map<String, String> data = currencies.getData();
        data.forEach((key, value) -> {
            String newKey = key.replace(to, "");
            result.put(newKey, value);
        });
        log.info("Currency rates from {} to {}: {}", to, froms, result);
        return result;
    }

    /**
     * Check if the given text is a valid currency code
     *
     * @param text The text to check
     * @return True if the text is a valid currency code, false otherwise
     */
    public boolean isCurrency(String text) {
        boolean isCurrency = CurrencyCodes.contains(text.toUpperCase());
        log.info("Checking if {} is a currency: {}", text, isCurrency);
        return isCurrency;
    }
}