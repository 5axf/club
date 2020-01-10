package com.sky.car.business.entity.user;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="用户信息" , description="用户信息")
public class UserRes {
	
	@ApiModelProperty(name="userid" , value="用户id")
	private Integer userid;
	@ApiModelProperty(name="nickName" , value="用户昵称")
	private String nickName;
	@ApiModelProperty(name="realName" , value="真实名称")
	private String realName;
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;
	@ApiModelProperty(name="expire" , value="时间戳")
	private long expire;
	@ApiModelProperty(name="expireString" , value="时间戳(字符串)")
	private String expireString;
	@ApiModelProperty(name="type" , value="用户类型,1普通用户，2会员用户")
    private String type;
	@ApiModelProperty(name="status" , value="是否可用,1普通用户可用，2普通用户不可用，3会员审核中，4会员可用，5会员不可用")
    private Integer status;
	@ApiModelProperty(name="mobile" , value="手机号码")
	private String mobile;
	@ApiModelProperty(name="avatarUrl" , value="用户头像")
    private String avatarUrl;
	@ApiModelProperty(name="country" , value="国家")
    private String country;
	@ApiModelProperty(name="province" , value="省份")
    private String province;
	@ApiModelProperty(name="city" , value="城市")
    private String city;
    @ApiModelProperty(name="gender" , value="用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer gender;
//    @ApiModelProperty(name="openid" , value="用户微信openid")
//    private String openid;
    @ApiModelProperty(name="level" , value="用户等级")
    private Integer level;

	@TableField(value="birthday" , fill= FieldFill.INSERT_UPDATE)
	@ApiModelProperty(name="birthday" , value="用户生日")
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
	private Date birthday;
	@ApiModelProperty(name="cardNo" , value="用户会员卡号")
	private String cardNo;
	@ApiModelProperty(name="balance" , value="用户账户余额")
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

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private  Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
    
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public long getExpire() {
		return expire;
	}
	public void setExpire(long expire) {
		this.expire = expire;
	}
	public String getExpireString() {
		return expireString;
	}
	public void setExpireString(String expireString) {
		this.expireString = expireString;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
//	public String getOpenid() {
//		return openid;
//	}
//	public void setOpenid(String openid) {
//		this.openid = openid;
//	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Override
	public String toString() {
		return "UserRes [userid=" + userid + ", nickName=" + nickName + ", realName=" + realName + ", token=" + token
				+ ", expire=" + expire + ", expireString=" + expireString + ", type=" + type + ", status=" + status
				+ ", mobile=" + mobile + ", avatarUrl=" + avatarUrl + ", country=" + country + ", province=" + province
				+ ", city=" + city + ", gender=" + gender + ", level=" + level + "]";
	}

}
