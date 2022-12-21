package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostRestaurantLoginRes {
    private int restaurantIdx;
    private String jwt;
}
