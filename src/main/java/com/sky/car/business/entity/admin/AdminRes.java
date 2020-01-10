package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="管理员信息" , description="管理员信息")
public class AdminRes {
	
	@ApiModelProperty(name="account" , value="账号")
	private String account;
	
	@ApiModelProperty(name="name" , value="管理员名称")
	private String name;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;
	
	@ApiModelProperty(name="expire" , value="时间戳")
	private String expire;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}
	
	

}
