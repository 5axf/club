package com.sky.car.business.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="用户参数" , description="用户参数")
public class LogoutUserReq {
	
	@ApiModelProperty(name="userid" , value="用户ID")
	private Integer userid;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;

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

}
