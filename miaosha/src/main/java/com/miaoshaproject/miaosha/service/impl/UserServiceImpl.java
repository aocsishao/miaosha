package com.miaoshaproject.miaosha.service.impl;

import com.miaoshaproject.miaosha.dao.UserDOMapper;
import com.miaoshaproject.miaosha.dao.UserPasswordDOMapper;
import com.miaoshaproject.miaosha.dataobject.UserDO;
import com.miaoshaproject.miaosha.dataobject.UserPasswordDO;
import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.service.UserService;
import com.miaoshaproject.miaosha.service.model.UserModel;
import com.miaoshaproject.miaosha.validator.ValidationResult;
import com.miaoshaproject.miaosha.validator.ValidatorImpl;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.validation.Validator;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdomapper获取对应的dataobject
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        if(userDO==null)return null;

        //通过用户Id获取y用户加密密码信息
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
//        if(StringUtils.isEmpty(userModel.getName())
//        ||userModel.getGender()==null || userModel.getAge()==null
//        ||StringUtils.isEmpty(userModel.getTelephone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        ValidationResult result=validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }


        //实现model->dataobject方法

        UserDO userDO=convertFromModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号以重复注册");
        }


        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO=convertPasswordFromModel(userModel);
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;

    }

    @Override
    public UserModel login(String telephone, String encrptpassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO=userDOMapper.selectByTelephone(telephone);
        if(userDO==null)throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);

        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel=convertFromDataObject(userDO, userPasswordDO);


        //比对用户信息内加密的密码是否和传输进来的密码匹配
        if(!StringUtils.equals(encrptpassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel==null)return null;
        UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel==null)return null;
        UserPasswordDO userPasswordDO=new UserPasswordDO();
        BeanUtils.copyProperties(userModel,userPasswordDO);
        return userPasswordDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO==null){
            return null;
        }
        UserModel userModel= new UserModel();

        BeanUtils.copyProperties(userDO,  userModel);
        if(userPasswordDO!=null) {
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }

}
