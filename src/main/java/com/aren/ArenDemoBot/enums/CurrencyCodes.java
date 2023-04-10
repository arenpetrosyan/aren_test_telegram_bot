package com.aren.ArenDemoBot.enums;

import java.util.Arrays;

public enum CurrencyCodes {
    DZD("DZD"),
    ARS("ARS"),
    AUD("AUD"),
    BDT("BDT"),
    BOB("BOB"),
    BRL("BRL"),
    GBP("GBP"),
    CAD("CAD"),
    CLP("CLP"),
    CNY("CNY"),
    COP("COP"),
    CRC("CRC"),
    CZK("CZK"),
    DKK("DKK"),
    EGP("EGP"),
    EUR("EUR"),
    GTQ("GTQ"),
    HNL("HNL"),
    HKD("HKD"),
    HUF("HUF"),
    ISK("ISK"),
    INR("INR"),
    IDR("IDR"),
    ILS("ILS"),
    JPY("JPY"),
    KES("KES"),
    KRW("KRW"),
    MOP("MOP"),
    MYR("MYR"),
    MXN("MXN"),
    NZD("NZD"),
    NIO("NIO"),
    NGN("NGN"),
    NOK("NOK"),
    PKR("PKR"),
    PYG("PYG"),
    PEN("PEN"),
    PHP("PHP"),
    PLN("PLN"),
    QAR("QAR"),
    RON("RON"),
    RUB("RUB"),
    SAR("SAR"),
    SGD("SGD"),
    ZAR("ZAR"),
    LKR("LKR"),
    SEK("SEK"),
    CHF("CHF"),
    TWD("TWD"),
    THB("THB"),
    TRY("TRY"),
    AED("AED"),
    USD("USD"),
    UYU("UYU"),
    VEF("VEF"),
    VND("VND");

    private final String code;

    CurrencyCodes(String s) {
        this.code = s;
    }

    public static boolean contains(String value) {
        return Arrays.stream(values()).anyMatch(e -> e.getCode().equals(value));
    }

    public String getCode() {
        return code;
    }
}
