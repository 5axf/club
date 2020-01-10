package com.sky.car.business.entity.order;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sky.car.business.entity.food.DishReq;

import java.util.Date;
import java.util.List;

public class AddOrderReq {

    private String token;

    private List<DishReq> list;

    private Double orderTotal;

    private Integer roomId;

    private Integer useNum;

    private Integer childNum;

    private String remark;

    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date orderDate;

    private Integer hoursId;

    private Integer isDish;


    public Integer getIsDish() {
        return isDish;
    }

    public void setIsDish(Integer isDish) {
        this.isDish = isDish;
    }

    public Integer getHoursId() {
        return hoursId;
    }

    public void setHoursId(Integer hoursId) {
        this.hoursId = hoursId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<DishReq> getList() {
        return list;
    }

    public void setList(List<DishReq> list) {
        this.list = list;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public Integer getChildNum() {
        return childNum;
    }

    public void setChildNum(Integer childNum) {
        this.childNum = childNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
