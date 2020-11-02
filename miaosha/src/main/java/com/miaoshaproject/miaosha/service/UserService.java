package com.miaoshaproject.miaosha.service;

import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.service.model.UserModel;

public interface UserService {
    //通过用户ID获取用户对象的方法
    public UserModel getUserById(Integer id);

    public void register(UserModel userModel) throws BusinessException;

    /*
    telephone是用户注册的手机
    password是用户加密后的密码
     */

    public UserModel login(String telephone, String encrptPassword) throws BusinessException;

}
