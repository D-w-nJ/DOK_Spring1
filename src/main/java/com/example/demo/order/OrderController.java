//package com.example.demo.order;
//
//import com.example.demo.config.BaseException;
//import com.example.demo.config.BaseResponse;
//import com.example.demo.config.BaseResponseStatus;
//import com.example.demo.utils.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
//
//@RestController
//@RequestMapping("app/orders")
//public class OrderController {
//
//    @Autowired
//    private final OrderProvider orderProvider;
//
//    @Autowired
//    private final OrderService orderService;
//
//    @Autowired
//    private final JwtService jwtService;
//
//    public OrderController(OrderProvider orderProvider, OrderService orderService, JwtService jwtService) {
//        this.orderProvider = orderProvider;
//        this.orderService = orderService;
//        this.jwtService = jwtService;
//    }
//
//    /**
//     * 찜 메뉴추가
//     */
//
//    @ResponseBody
//    @PostMapping("/{userIdx}/wishes")
//    public BaseResponse<PostWishRes> addWish(@PathVariable("userIdx") int userIdx, @RequestBody PostWishReq postWishReq) throws BaseException {
//        try{
//            int userIdxByJwt = jwtService.getUserIdx();
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//        }catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()))
//        }
//
//    }
//}
