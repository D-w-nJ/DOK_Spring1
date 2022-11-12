package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.restaurant.model.PostRestaurantReq;
import com.example.demo.src.restaurant.model.PostRestaurantRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    private final RestaurantDao restaurantDao;
    private final RestaurantProvider restaurantProvider;

    @Autowired
    public RestaurantService(RestaurantDao restaurantDao, RestaurantProvider restaurantProvider) {
        this.restaurantDao = restaurantDao;
        this.restaurantProvider = restaurantProvider;
    }


    // **************************************************8
    // 회원가입 (POST)
    public PostRestaurantRes createRestaurant(PostRestaurantReq postRestaurantReq) throws BaseException
    {
        try{
            int restaurantIdx = restaurantDao.createRestaurant(postRestaurantReq);
            return new PostRestaurantRes(restaurantIdx);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
