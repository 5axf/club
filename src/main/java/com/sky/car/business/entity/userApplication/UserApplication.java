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
@TableName("ty_user_application")
public class UserApplication {
    private static final long serialVersionUID = 1L;

    @TableId(value = "userApplicationId", type = IdType.AUTO)
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
