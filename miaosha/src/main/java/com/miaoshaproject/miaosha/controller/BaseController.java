package com.miaoshaproject.miaosha.controller;

import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BaseController {


    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    //定义exceptionhandler解决未被controller层吸收的exception异常
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerBusinessException(HttpServletRequest request, BusinessException ex){
        BusinessException businessException=ex;
        Map<String, Object> responseData=new HashMap<>();
        responseData.put("errCode", businessException.getErrCode());
        responseData.put("errMsg", businessException.getErrMsg());

        return CommonReturnType.create(responseData, "fail");

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String, Object> responseData=new HashMap<>();
        responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
        responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());

        return CommonReturnType.create(responseData, "fail");

    }


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerNoHandleFoundException(HttpServletRequest request, NoHandlerFoundException ex){
        Map<String, Object> responseData=new HashMap<>();

        responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
        responseData.put("errMsg", "改URL访问路径不存在");

        return CommonReturnType.create(responseData, "fail");

    }
}
