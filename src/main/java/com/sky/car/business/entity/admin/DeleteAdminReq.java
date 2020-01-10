package com.sky.car.business.entity.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="管理员删除参数" , description="管理员")
public class DeleteAdminReq {
	
	@ApiModelProperty(name="id" , value="管理员Id")
	private Integer id;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
