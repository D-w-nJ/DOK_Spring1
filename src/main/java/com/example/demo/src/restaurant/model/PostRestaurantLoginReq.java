package com.example.demo.src.restaurant.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRestaurantLoginReq {
    private String restaurantName;
    private String password;
}
