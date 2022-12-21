package com.example.demo.src.restaurant;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class RestaurantProvider {

    private final RestaurantDao restaurantDao;
    private final JwtService jwtService;

    @Autowired
    public RestaurantProvider(RestaurantDao restaurantDao, JwtService jwtService) {
        this.restaurantDao = restaurantDao;
        this.jwtService =jwtService;
    }

    public PostRestaurantLoginRes logIn(PostRestaurantLoginReq postRestaurantLoginReq)throws BaseException{
        Restaurant restaurant = restaurantDao.getPwd(postRestaurantLoginReq);
        String password;
        try{
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(restaurant.getPassword());
        }catch(Exception ignored){
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        if(postRestaurantLoginReq.getPassword().equals(password)){
            String restaurantName = postRestaurantLoginReq.getRestaurantName();
            String jwt = jwtService.createRestaurantJwt(restaurantName);
            int restaurantIdx = restaurantDao.getPwd(postRestaurantLoginReq).getRestaurantIdx();
            return new PostRestaurantLoginRes(restaurantIdx,jwt);
        }else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // 모든 restaurant 조회
    public List<GetRestaurantRes> getRestaurants() throws BaseException {
        try{
            List<GetRestaurantRes> getRestaurantRes = restaurantDao.getRestaurants();
            return getRestaurantRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 category를 가진 restaurant 조회
    public List<GetRestaurantRes> getRestaurantsByCategory(String category) throws BaseException {
        try{
            List<GetRestaurantRes> getRestaurantRes = restaurantDao.getRestaurantByCategory(category);
            return getRestaurantRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // size 단위로 restaurant page 출력
    public List<GetRestaurantRes> getRestPage(int size) throws BaseException {
        try{
            List<GetRestaurantRes> getRestaurantRes = restaurantDao.getRestPage(size);
            return getRestaurantRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 커서 기반 restaurant page 출력
    public List<GetRestaurantRes> getRestBasedCursorPage(int cursorId, int size)throws BaseException{
        try{
            List<GetRestaurantRes> getRestaurantRes = restaurantDao.getRestBasedCursor(cursorId,size);
            return getRestaurantRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 지점별 메뉴 제공
    public List<GetMenuRes> getMenus(String restaurantName)throws BaseException{
        try{
            List<GetMenuRes> getMenuRes = restaurantDao.getMenus(restaurantName);
            return getMenuRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
