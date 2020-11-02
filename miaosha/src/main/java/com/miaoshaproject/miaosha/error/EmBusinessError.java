package com.miaoshaproject.miaosha.error;

public enum EmBusinessError implements CommonError {

    //通用错误类型00001
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),


    //10000开头为用户信息相关错误定义
    USER_NOT_EXIST(10002,"用户不存在"),

    //未知错误
    UNKNOWN_ERROR(20001, "未知错误"),
    //登录错误
    USER_LOGIN_FAIL(20002, "用户手机后或密码不正确"),

    USER_NOT_LOGIN(20003,"用户还未登录"),
    //3000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(30000,"库存不足")
    ;

    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode, String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
