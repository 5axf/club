package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="管理员详细信息" , description="管理员信息")
public class AdminInfoRes {
	
	@ApiModelProperty(name="account" , value="账号")
	private String account;
	
	@ApiModelProperty(name="name" , value="管理员名称")
	private String name;
	
	@ApiModelProperty(name="id" , value="管理员ID")
	private Integer id;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
