package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Restaurant {
    private int RestaurantIdx;
    private String RestaurantName;
    private String password;
    private String address;
    private String category;
}
