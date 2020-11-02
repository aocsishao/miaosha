package com.miaoshaproject.miaosha.controller;

import com.miaoshaproject.miaosha.controller.viewobject.UserVO;
import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.response.CommonReturnType;
import com.miaoshaproject.miaosha.service.UserService;
import com.miaoshaproject.miaosha.service.model.UserModel;
import com.miaoshaproject.miaosha.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.miaoshaproject.miaosha.error.EmBusinessError.PARAMETER_VALIDATION_ERROR;

@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户注册接口
    @ResponseBody
    @RequestMapping(value = "/register",method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") Integer gender,
                                     @RequestParam(name="age") Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号是否与对应的otpCode是否符合
        String inSessionOtpCode=(String)this.httpServletRequest.getSession().getAttribute(telephone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException( PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }

        //注册流程
        UserModel userModel=new UserModel();
        userModel.setName(name);
        userModel.setRegisterMode("byphone");
        userModel.setTelephone(telephone);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);

    }

    public String EncodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        Base64 base64en=new Base64();

        //加密字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户登录接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone") String telephone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)){
            throw new BusinessException(PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务
        UserModel userModel=userService.login(telephone, this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的Session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
        return CommonReturnType.create(null);
    }





    //用户获取otp短信的接口

    @RequestMapping(value = "/getotp",method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone){

        //需要按照一规则生成验证码
        Random random=new Random();
        int randomInt=random.nextInt(99999);
        randomInt +=10000;
        String otpCode=String.valueOf(randomInt);



        //将OTP验 证码同对应用户的手机号相关联,使用httpSession的方式绑定他的手机号与OTPCODE
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        String s=(String)this.httpServletRequest.getSession().getAttribute(telephone);


        //将OTP验证码通过短信通过发送给用户，省略
        System.out.println("telphone ="+telephone+"&otpCode="+s);
        return CommonReturnType.create(null);

    }

    @RequestMapping("/otp")
    public String getOpt(){
        return "getotp.html";
    }

    @RequestMapping("/register")
    public String register(){
        return "register.html";
    }

    @RequestMapping("/login")
    public String login(){
        return "login.html";
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取相对应id的用户对象并返回给前端

        UserModel userModel=userService.getUserById(id);
        //若获取的对应用户信息不存在
        if(userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO=convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }


    private UserVO convertFromModel(UserModel userModel){
        if(userModel==null)return null;
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}
