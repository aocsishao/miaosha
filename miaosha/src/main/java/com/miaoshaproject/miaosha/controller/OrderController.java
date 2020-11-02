package com.miaoshaproject.miaosha.controller;

import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.response.CommonReturnType;
import com.miaoshaproject.miaosha.service.OrderService;
import com.miaoshaproject.miaosha.service.model.OrderModel;
import com.miaoshaproject.miaosha.service.model.UserModel;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId", required = false) Integer promoId) throws BusinessException {

        //获取用户的登录信息
        Boolean isLogin= (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin==null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "用户还未登录，还未下单");
        }
        UserModel userModel=(UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel order = orderService.createOrder(userModel.getId(), itemId, amount, promoId);
        return  CommonReturnType.create(null);
    }
}
