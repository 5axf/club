package com.sky.car.business.entity.order;

import com.baomidou.mybatisplus.annotations.TableField;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author axf
 * @since 2020-01-09
 */
@TableName("ty_order_dish")
public class OrderDish implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增Id
     */
    @TableId(value = "orderDishId", type = IdType.AUTO)
    @ApiModelProperty(name="orderDishId" , value="订单详情id")
    private Integer orderDishId;
    @ApiModelProperty(name="orderId" , value="订单id")
    private Integer orderId;
    @ApiModelProperty(name="orderNum" , value="订单编号")
    private String orderNum;
    @ApiModelProperty(name="dishId" , value="菜品id")
    private Integer dishId;
    @ApiModelProperty(name="dishName" , value="菜品名称")
    private Integer dishName;
    @ApiModelProperty(name="price" , value="菜品单价")
    @JSONField(format = "#.00")
    private BigDecimal price;
    @ApiModelProperty(name="num" , value="菜品数量")
    private Integer num;
    @ApiModelProperty(name="state" , value="菜品状态：1、有效；2、无效")
    private Integer state;
    @TableField(value="createTime" , fill= FieldFill.INSERT)
    @ApiModelProperty(name="creatTime" , value="记录创建时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date creatTime;
    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name="updateTime" , value="记录修改时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getOrderDishId() {
        return orderDishId;
    }

    public void setOrderDishId(Integer orderDishId) {
        this.orderDishId = orderDishId;
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

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getDishName() {
        return dishName;
    }

    public void setDishName(Integer dishName) {
        this.dishName = dishName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
