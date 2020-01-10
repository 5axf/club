package com.sky.car.business.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="微信用户信息" , description="微信用户信息")
public class WxUserInfoRes {
	@ApiModelProperty(name="avatarUrl" , value="微信用户头像")
	private String avatarUrl;
	@ApiModelProperty(name="city" , value="用户所在城市")
	private String city;
	@ApiModelProperty(name="country" , value="用户所在国家")
	private String country;
	@ApiModelProperty(name="gender" , value="用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
	private int gender;
	@ApiModelProperty(name="nickName" , value="用户昵称")
	private String nickName;
	@ApiModelProperty(name="province" , value="用户所在省份")
	private String province;
	
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

}
