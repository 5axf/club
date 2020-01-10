package com.sky.car.business.entity.user;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author 余晓翔
 * @since 2019-04-03
 */
@TableName("ty_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "userid", type = IdType.AUTO)
    private Integer userid;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * '用户类型,1普通用户，2会员用户',
     */
    private String type;
    /**
     * 是否可用,1普通用户可用，2普通用户不可用，3会员审核中，4会员可用，5会员不可用
     */
    private Integer status;
    /**
     * 真实姓名
     */
    private String realName;

    private String email;
    /**
     * 联系电话
     */
    private String mobile;
    @TableField(value="createTime" , fill=FieldFill.INSERT)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	
    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String token;
    /**
     * 昵称
     */
    private String nickName;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String openid;
    private Integer sex; // 0：男；1：女

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
     * 账户余额
     */
    private BigDecimal balance;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }


    @Override
    public String toString() {
        return "User{" +
        ", userid=" + userid +
        ", username=" + username +
        ", password=" + password +
        ", type=" + type +
        ", status=" + status +
        ", realName=" + realName +
        ", email=" + email +
        ", mobile=" + mobile +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", token=" + token +
        ", nickName=" + nickName +
        ", avatarUrl=" + avatarUrl +
        ", country=" + country +
        ", province=" + province +
        ", city=" + city +
        ", openid=" + openid +
        "}";
    }
}
