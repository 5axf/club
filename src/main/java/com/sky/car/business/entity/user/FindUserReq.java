package com.sky.car.business.entity.user;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value="查询用户" , description="查询用户信息")
public class FindUserReq {
	
	@ApiModelProperty(name="userName" , value="用户名称")
	private String userName;
	
	@ApiModelProperty(name="status" , value="状态 0是不可用 1是可用")
	private Integer status;
	
	@ApiModelProperty(name="token" , value="token令牌")
	private String token;
	
	@ApiModelProperty(name="current" , value="当前页码")
	private int current;
	
	@ApiModelProperty(name="size" , value="每页记录")
	private int size;

	@TableField(value="birthday" , fill= FieldFill.INSERT_UPDATE)
	@ApiModelProperty(name="birthday" , value="用户生日")
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd")
	private Date birthday;
	@ApiModelProperty(name="cardNo" , value="用户会员卡号")
	private String cardNo;

	private String type;
	private String mobile;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
