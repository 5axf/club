package com.sky.car.business.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="微信绑定手机号" , description="微信绑定手机号")
public class BindPhoneReq {
	
	@ApiModelProperty(name="userid" , value="用户ID")
	private Integer userid;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;
	
	@ApiModelProperty(name="encryptedData" , value="包括敏感数据在内的完整用户信息的加密数据")
	private String encryptedData;
	
	@ApiModelProperty(name="iv" , value="加密算法的初始向量")
	private String iv;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

}
