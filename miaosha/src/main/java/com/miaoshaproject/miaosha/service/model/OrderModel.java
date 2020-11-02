package com.miaoshaproject.miaosha.service.model;

import java.math.BigDecimal;

//用户下单的交易模型
public class OrderModel {
    //20201020+....
    private String id;

    private Integer userId;

    private Integer itemId;

    private Integer amount;

    //若promoId非空，则表示秒杀商品价格
    private BigDecimal orderPrice;

    //商品购买当时的价格,若promoId非空，则表示秒杀商品价格
    private BigDecimal itemPrice;

    //若非空则是以秒杀商品方式下单
    private Integer promoId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }


    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
