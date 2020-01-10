package com.sky.car.business.entity.food;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.util.Date;

public class DishReq {

    @ApiModelProperty(name="current" , value="当前页码")
    private int current;

    @ApiModelProperty(name="size" , value="每页记录")
    private int size;

    private String token;

    private Integer dishId;

    private String dishName;

    private String dishDesc;

    private String dishImg;

    private Double price;

    private Integer firstMenuId;

    private Integer twoMenuId;

    private String menuName;

    private Integer isFrame;

    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Date updateTime;

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishDesc() {
        return dishDesc;
    }

    public void setDishDesc(String dishDesc) {
        this.dishDesc = dishDesc;
    }

    public String getDishImg() {
        return dishImg;
    }

    public void setDishImg(String dishImg) {
        this.dishImg = dishImg;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getFirstMenuId() {
        return firstMenuId;
    }

    public void setFirstMenuId(Integer firstMenuId) {
        this.firstMenuId = firstMenuId;
    }

    public Integer getTwoMenuId() {
        return twoMenuId;
    }

    public void setTwoMenuId(Integer twoMenuId) {
        this.twoMenuId = twoMenuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(Integer isFrame) {
        this.isFrame = isFrame;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
