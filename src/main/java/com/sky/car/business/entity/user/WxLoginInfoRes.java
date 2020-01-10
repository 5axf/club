package com.sky.car.business.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="微信登录信息" , description="微信登录信息")
public class WxLoginInfoRes {
	
	@ApiModelProperty(name="code" , value="微信请求code")
    private String code;
	@ApiModelProperty(name="iv" , value="微信请求iv")
    private String iv;
	@ApiModelProperty(name="encryptedData" , value="微信请求encryptedData")
    private String encryptedData;
	@ApiModelProperty(name="signature" , value="微信请求signature")
    private String signature;
	@ApiModelProperty(name="userInfo" , value="微信用户信息")
    private WxUserInfoRes userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserInfo(WxUserInfoRes userInfo) {
        this.userInfo = userInfo;
    }
    
	public WxUserInfoRes getUserInfo() {
		return userInfo;
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

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
