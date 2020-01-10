package com.sky.car.business.entity.userApplication;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>
 * 会员申请
 * </p>
 *
 * @author 刘鹏
 * @since 2020-01-08
 */
public class UserApplicationReq {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name="userApplicationId" , value="会员申请记录id")
    private Integer userApplicationId;

    @ApiModelProperty(name="userId" , value="用户id")
    private Integer userId;

    @ApiModelProperty(name="status" , value="审核状态：1、审核中；2、审核通过；3审核不通过")
    private Integer status;

    @TableField(value="createTime" , fill= FieldFill.INSERT)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name="createTime" , value="创建时间")
    private Date createTime;

    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name="updateTime" , value="修改时间")
    private Date updateTime;

    @ApiModelProperty(name="token" , value="token令牌")
    private String token;

    @ApiModelProperty(name="current" , value="当前页码")
    private int current;

    @ApiModelProperty(name="size" , value="每页记录")
    private int size;

    /**
     * 生日
     */
    @TableField(value="birthday" , fill=FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
    private Date birthday;
    /**
     * 会员卡号
     */
    private String cardNo;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 真实姓名
     */
    private String realName;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Integer getUserApplicationId() {
        return userApplicationId;
    }

    public void setUserApplicationId(Integer userApplicationId) {
        this.userApplicationId = userApplicationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
