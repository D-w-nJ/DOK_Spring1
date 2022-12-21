package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.utils.AES128;
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
            //암호화 : postRestaurantReq에서 받은 비밀번호를 보안을 위해 암호화
            String pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postRestaurantReq.getPassword());
            postRestaurantReq.setPassword(pwd);
        }catch (Exception ignored){
            throw new BaseException(BaseResponseStatus.PASSWORD_DECRYPTION_ERROR);
        }
        try{
            int restaurantIdx = restaurantDao.createRestaurant(postRestaurantReq);
            return new PostRestaurantRes(restaurantIdx);
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 메뉴추가 (POST)
    public PostMenuRes createMenu(String restaurantName,PostMenuReq postMenuReq) throws BaseException{
        try{
            PostMenuRes postMenuRes = restaurantDao.createMenu(restaurantName,postMenuReq);
            return postMenuRes;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 지점 정보변경
    public PatchRestRes modifyRest(String restaurantName,PatchRestReq patchRestReq)throws BaseException{
        try{
            PatchRestRes patchRestRes = restaurantDao.modifyRest(restaurantName, patchRestReq);
            return patchRestRes;
        }catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
