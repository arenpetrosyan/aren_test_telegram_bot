package com.aren.ArenDemoBot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonIgnoreProperties
public class Currency {
    String status;
    String message;
    HashMap<String, String> data;
}
