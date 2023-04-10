package com.aren.ArenDemoBot.service;

import com.aren.ArenDemoBot.model.Currency;
import com.aren.ArenDemoBot.rest.CurrencyRestMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CurrencyService.class})
@ExtendWith(SpringExtension.class)
class CurrencyServiceTest {
    @MockBean
    private CurrencyRestMap currencyRestMap;
    @Autowired
    private CurrencyService currencyService;
    @Test
    void testGetCurrencyRate() {
        Currency currency = new Currency();
        currency.setData(new HashMap<>());

        when(currencyRestMap.getCurrencyRate(anyString(), anyString())).thenReturn(currency);

        assertNull(currencyService.getCurrencyRate("random text", "random text"));
        verify(currencyRestMap).getCurrencyRate(anyString(), anyString());
    }
    @Test
    void testGetCurrencyRates() {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("RUBEUR", "EURRUB");
        Currency currency = new Currency();
        currency.setData(dataMap);

        when(currencyRestMap.getCurrencyRates(any(), anyString())).thenReturn(currency);
        Map<String, String> actualCurrencyRates = currencyService.getCurrencyRates(new ArrayList<>(), "random text");

        assertEquals(1, actualCurrencyRates.size());
        assertEquals("EURRUB", actualCurrencyRates.get("RUBEUR"));
        verify(currencyRestMap).getCurrencyRates(any(), anyString());
    }
    @Test
    void testIsCurrency() {
        assertFalse(currencyService.isCurrency("Text"));
        assertTrue(currencyService.isCurrency("DZD"));
    }
}