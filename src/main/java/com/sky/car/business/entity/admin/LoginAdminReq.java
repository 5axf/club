package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="管理员登录参数" , description="管理员")
public class LoginAdminReq {
	
	@ApiModelProperty(name="account" , value="账号")
	private String account;
	
	@ApiModelProperty(name="password" , value="密码")
	private String password;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
