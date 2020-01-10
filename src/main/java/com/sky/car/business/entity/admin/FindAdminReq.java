package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="查询管理员" , description="查询管理员")
public class FindAdminReq {

	@ApiModelProperty(name="account" , value="账号")
	private String account;
	
	@ApiModelProperty(name="name" , value="名称")
	private String name;
	
	@ApiModelProperty(name="status" , value="状态 0是不可用 1是可用")
	private Integer status;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;
	
	@ApiModelProperty(name="current" , value="当前页码")
	private int current;
	
	@ApiModelProperty(name="size" , value="每页记录")
	private int size;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
}
