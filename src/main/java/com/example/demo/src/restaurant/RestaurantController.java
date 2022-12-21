package com.example.demo.src.restaurant;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_RESTAURANT_JWT;
import static com.example.demo.config.BaseResponseStatus.POST_REST_EMPTY_NAME;

@Controller
@RequestMapping("/app/restaurants")
public class RestaurantController {

    @Autowired
    private final RestaurantProvider restaurantProvider;
    @Autowired
    private final RestaurantService restaurantService;
    @Autowired
    private final JwtService jwtService;

    public RestaurantController(RestaurantProvider restaurantProvider, RestaurantService restaurantService, JwtService jwtService) {
        this.restaurantProvider = restaurantProvider;
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
    }

    //********************************************************************

    /**
     * 지점 등록 API
     * [POST] /restaurants
     */

    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<PostRestaurantRes> createRestaurant(@RequestBody PostRestaurantReq postRestaurantReq){
        // 지점 등록 이름이 null값이 아닌지 간단한 validation
        if(postRestaurantReq.getRestaurantName()==null){
            return new BaseResponse<>(POST_REST_EMPTY_NAME);
        }

        try{
            PostRestaurantRes postRestaurantRes = restaurantService.createRestaurant(postRestaurantReq);
            return  new BaseResponse<>(postRestaurantRes);
        }catch (BaseException exception){
            return  new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 음식점 로그인 API
     * [POST] /restaurants/login
     */
    @ResponseBody
    @PostMapping("/log-in")
    public BaseResponse<PostRestaurantLoginRes> restaurantLogin(@RequestBody PostRestaurantLoginReq postRestaurantLoginReq){
        try{
            PostRestaurantLoginRes postRestaurantLoginRes = restaurantProvider.logIn(postRestaurantLoginReq);
            return new BaseResponse<>(postRestaurantLoginRes);
        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());}
    }


    /**
     * 모든 지점 조회 API
     * [GET]/restaurant
     *
     * 카테고리별 지점 조회 API
     * [GET]/restaurant?category=
     */

    // @ResponseBody
    // 기존 리턴값 BaseResponse<List<GetRestaurantRes>>
    @GetMapping("")
    public String getRestaurants(@RequestParam(required = false) String category, Model model){
        try{
            if(category==null){
                List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestaurants();
                model.addAttribute("restaurantsList",getRestaurantRes);
                return "restaurantList";
            }
            List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestaurantsByCategory(category);
            model.addAttribute("restaurantsList",getRestaurantRes);
            return "restaurantList";
        }catch(BaseException exception){
            return "asdf";
        }
//        try{
//            if(category==null){
//                List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestaurants();
//                return new BaseResponse<>(getRestaurantRes);
//            }
//            List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestaurantsByCategory(category);
//            return new BaseResponse<>(getRestaurantRes);
//        }catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
    }

    /**
     * 커서 기반 페이네이션
     * [GET] app/restaurant/pages
     *  cursorId로 파라미터 가져와서 보다 작은 5개 출력, 파라미터 없다면 최신 지점 5개 출력
     */

   @ResponseBody
    @GetMapping("/pages")
    public BaseResponse<List<GetRestaurantRes>> getRestaurantsBasedCursor(@RequestParam(required = false,defaultValue = "-1") int cursorId) throws BaseException {
        try{
            int size = 5;
            if(cursorId==-1){
                List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestPage(size);
                return new BaseResponse<>(getRestaurantRes);
            }
            List<GetRestaurantRes> getRestaurantRes = restaurantProvider.getRestBasedCursorPage(cursorId,size);
            return new BaseResponse<>(getRestaurantRes);
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 메뉴 추가 API
     * [POST]/restaurants/menus/:restaurantName
     */
    @ResponseBody
    @PostMapping("/menus/{restaurantName}")
    public BaseResponse<PostMenuRes> postMenu(@PathVariable("restaurantName") String restaurantName, @RequestBody PostMenuReq postMenuReq){
        try{
            String restaurantNameByJwt = jwtService.getRestaurantName();
            if(!restaurantName.equals(restaurantNameByJwt)){
                return new BaseResponse<>(INVALID_RESTAURANT_JWT);
            }
            PostMenuRes postMenuRes = restaurantService.createMenu(restaurantName,postMenuReq);
            return new BaseResponse<>(postMenuRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 지점별 메뉴 조회 API
     * [GET] /restaurants/menus/:restaurantName
     */
    // @ResponseBody
    // BaseResponse<List<GetMenuRes>>
    @GetMapping("/menus/{restaurantName}")
    public String getMenu(@PathVariable("restaurantName") String restaurantName, Model model){
        try{
            List<GetMenuRes> getMenuRes = restaurantProvider.getMenus(restaurantName);
            model.addAttribute("menuList", getMenuRes);
            return "menuList";
        }catch (BaseException exception){
            return "asfdf";
        }
//        try{
//            List<GetMenuRes> getMenuRes = restaurantProvider.getMenus(restaurantName);
//            return new BaseResponse<>(getMenuRes);
//        }catch (BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
    }

    /**
     * 지점 정보 변경 API
     * [PATCH] /restaurants/:restaurantName
     */
    @ResponseBody
    @PatchMapping("/{restaurantName}")
    public BaseResponse<PatchRestRes> modifyRest(@PathVariable("restaurantName")String restaurantName, @RequestBody PatchRestReq patchRestReq){
        try{
            String restaurantNameByJwt = jwtService.getRestaurantName();
            if(!restaurantName.equals(restaurantNameByJwt)){
                return new BaseResponse<>(INVALID_RESTAURANT_JWT);
            }
            PatchRestRes patchRestRes = restaurantService.modifyRest(restaurantName, patchRestReq);
            return new BaseResponse<>(patchRestRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
