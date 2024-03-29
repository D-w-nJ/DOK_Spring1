package com.example.demo.src.restaurant.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRestaurantReq {
    private String restaurantName;
    private String password;
    private String address;
    private String category;
}
