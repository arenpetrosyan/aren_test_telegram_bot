package com.aren.ArenDemoBot.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
public class User {

    // MongoDB ID for the user
    @MongoId
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String city;
    private String currency;
}