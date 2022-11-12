package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.restaurant.model.GetRestaurantRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantProvider {

    private final RestaurantDao restaurantDao;

    @Autowired
    public RestaurantProvider(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
    }

    // 모든 restaurant 조회
    public List<GetRestaurantRes> getRestaurants() throws BaseException {
        try{
            List<GetRestaurantRes> getRestaurantRes = restaurantDao.getRestaurants();
            return getRestaurantRes;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 해당 category를 가진 restaurant 조회
    public List<GetRestaurantRes> getRestaurantsByCategory(String category) throws BaseException {
        try{
            List<GetRestaurantRes> getRestaurantRes = restaurantDao.getRestaurantByCategory(category);
            return getRestaurantRes;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
