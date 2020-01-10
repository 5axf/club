package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="添加管理员信息" , description="添加管理员信息")
public class SaveAdminReq {
	
	@ApiModelProperty(name="account" , value="账号")
	private String account;
	
	@ApiModelProperty(name="password" , value="密码")
	private String password;
	
	@ApiModelProperty(name="name" , value="管理员名称")
	private String name;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
