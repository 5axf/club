package com.sky.car.business.entity.recharge;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sky.car.business.entity.user.User;
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
 * @since 2020-01-08
 */
public class RechargeRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name="rechargeId" , value="充值记录id")
    private Integer rechargeId;
    @ApiModelProperty(name="outTradeNo" , value="充值订单号")
    private String  outTradeNo;
    @ApiModelProperty(name="userId" , value="用户id")
    private Integer userId;
    @ApiModelProperty(name="amount" , value="充值金额")
    private BigDecimal amount;
    @ApiModelProperty(name="payType" , value="充值方式：1、微信支付")
    private Integer payType;
    @ApiModelProperty(name="balance" , value="充值前账户余额")
    private BigDecimal balance;
    @ApiModelProperty(name="status" , value="状态：1、充值成功；2、充值失败")
    private Integer status;
    @ApiModelProperty(name="transactionId" , value="充值成功微信返回的订单号")
    private String transactionId;
    @TableField(value="createTime" , fill= FieldFill.INSERT)
    @ApiModelProperty(name="createTime" , value="记录创建时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name="updateTime" , value="记录修改时间")
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(Integer rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
