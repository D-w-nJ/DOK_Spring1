package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMenuRes {
    private int menuIdx;
    private String restaurantName;
    private String menuName;
    private int price;
}
