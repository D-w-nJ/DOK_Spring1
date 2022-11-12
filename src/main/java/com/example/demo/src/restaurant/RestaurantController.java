package com.example.demo.src.restaurant;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.restaurant.model.GetRestaurantRes;
import com.example.demo.src.restaurant.model.PostRestaurantReq;
import com.example.demo.src.restaurant.model.PostRestaurantRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/app/restaurants")
public class RestaurantController {

    @Autowired
    private final RestaurantProvider restaurantProvider;
    @Autowired
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantProvider restaurantProvider, RestaurantService restaurantService) {
        this.restaurantProvider = restaurantProvider;
        this.restaurantService = restaurantService;
    }

    //********************************************************************

    /**
     * 등록 API
     * [POST] /restaurants
     */

    @ResponseBody
    @PostMapping("/launching")
    public BaseResponse<PostRestaurantRes> createRestaurant(@RequestBody PostRestaurantReq postRestaurantReq){
        try{
            PostRestaurantRes postRestaurantRes = restaurantService.createRestaurant(postRestaurantReq);
            return  new BaseResponse<>(postRestaurantRes);
        }catch (BaseException exception){
            return  new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 모든 지점 조회 API
     * [GET]/restaurant
     *
     * 카테고리별 지점 조회 API
     * [GET]/restaurant?category=
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetRestaurantRes>> getRestaurants(@RequestParam(required = false) String category){
        try{
            if(category==null){
                List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestaurants();
                return new BaseResponse<>(getRestaurantRes);
            }
            List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestaurantsByCategory(category);
            return new BaseResponse<>(getRestaurantRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
