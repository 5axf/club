package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="管理员参数" , description="管理员参数")
public class LogoutAdminReq {
	
	@ApiModelProperty(name="account" , value="账号")
	private String account;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
