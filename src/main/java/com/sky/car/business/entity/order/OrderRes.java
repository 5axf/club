package com.sky.car.business.entity.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author axf
 * @since 2020-01-09
 */
public class OrderRes {

    @ApiModelProperty(name="orderId" , value="订单id")
    private Integer orderId;
    @ApiModelProperty(name="orderNum" , value="订单编号")
    private String orderNum;
    @ApiModelProperty(name="userId" , value="用户id")
    private Integer userId;
    @ApiModelProperty(name="phone" , value="预定人电话")
    private String phone;
    @ApiModelProperty(name="roomId" , value="房间id")
    private Integer roomId;
    @ApiModelProperty(name="roomName" , value="房间名")
    private String roomName;
    @ApiModelProperty(name="useNum" , value="预定人数")
    private Integer useNum;
    @ApiModelProperty(name="orderTotal" , value="订单总金额")
    @JSONField(format = "#.00")
    private BigDecimal orderTotal;
    @ApiModelProperty(name="payWey" , value="支付方式 1：线上；2：线下")
    private Integer payWey;
    @ApiModelProperty(name="hoursId" , value="时间段id")
    private Integer hoursId;
    @ApiModelProperty(name="hourTime" , value="时间范围")
    private String hourTime;
    @ApiModelProperty(name="state" , value="订单状态")
    private Integer state;
    @ApiModelProperty(name="remark" , value="订单备注")
    private String remark;
    @ApiModelProperty(name="childNum" , value="小孩数量")
    private Integer childNum;
    @TableField(value="creatTime" , fill= FieldFill.INSERT)
    @ApiModelProperty(name="creatTime" , value="记录创建时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date creatTime;
    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name="updateTime" , value="记录修改时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<OrderDish> orderDishList;

    public List<OrderDish> getOrderDishList() {
        return orderDishList;
    }

    public void setOrderDishList(List<OrderDish> orderDishList) {
        this.orderDishList = orderDishList;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Integer getPayWey() {
        return payWey;
    }

    public void setPayWey(Integer payWey) {
        this.payWey = payWey;
    }

    public Integer getHoursId() {
        return hoursId;
    }

    public void setHoursId(Integer hoursId) {
        this.hoursId = hoursId;
    }

    public String getHourTime() {
        return hourTime;
    }

    public void setHourTime(String hourTime) {
        this.hourTime = hourTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getChildNum() {
        return childNum;
    }

    public void setChildNum(Integer childNum) {
        this.childNum = childNum;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
