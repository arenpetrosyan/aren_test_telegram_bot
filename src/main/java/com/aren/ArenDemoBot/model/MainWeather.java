package com.aren.ArenDemoBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MainWeather {
    private Integer temp;
    @JsonProperty("feels_like")
    private Integer feelsLike;
}
