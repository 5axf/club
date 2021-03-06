package com.sky.car.business.entity.order;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
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
@TableName("ty_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增Id
     */
    @TableId(value = "orderId", type = IdType.AUTO)
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
    @TableField(value="creaTime" , fill= FieldFill.INSERT)
    @ApiModelProperty(name="creaTime" , value="记录创建时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date creaTime;
    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name="updateTime" , value="记录修改时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(value="orderDate" , fill=FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date orderDate;

    private Integer isDish;

    private BigDecimal curBalance;

    @TableField(value="payTime" , fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name="payTime" , value="记录修改时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(BigDecimal curBalance) {
        this.curBalance = curBalance;
    }

    public Integer getIsDish() {
        return isDish;
    }

    public void setIsDish(Integer isDish) {
        this.isDish = isDish;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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

    public Date getCreaTime() {
        return creaTime;
    }

    public void setCreaTime(Date creaTime) {
        this.creaTime = creaTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
