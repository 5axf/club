package com.sky.car.business.api.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.user.BindPhoneReq;
import com.sky.car.business.entity.user.LogoutUserReq;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.entity.user.UserRes;
import com.sky.car.business.entity.user.WxLoginInfoRes;
import com.sky.car.business.entity.user.WxUserInfoRes;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.UserToken;
import com.sky.car.common.Constant;
import com.sky.car.common.Result;
import com.sky.car.util.DateUtil;
import com.sky.car.util.EmojiFilter;
import com.sky.car.util.JSONUtil;
import com.sky.car.util.Utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/wx/auth/")
@Validated
@Api(tags = { "小程序-微信接口" })
public class WxAuthController {
	
    private final Log logger = LogFactory.getLog(WxAuthController.class);

    @Autowired
    private WxMaService wxService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserTokenService tokenService;

    @ApiOperation(value="微信登录" , notes="" , response= UserRes.class , httpMethod = "POST")
    @PostMapping("login")
    public Result loginByWeixin(@RequestBody WxLoginInfoRes body, HttpServletRequest request) {
		
        String code = body.getCode();
        WxUserInfoRes userInfo = body.getUserInfo();
        if (code == null || userInfo == null) {
            return Result.badArgumentResult();
        }
        
        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            logger.info("微信登录接口返回："+JSONUtil.toJSON(result));
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("微信登录接口异常",e);
        }

        if (sessionKey == null || openId == null) {
            return Result.failResult();
        }
        
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("openid", openId);
        //查询数据库的user
        User user = userService.selectOne(wrapper);
        if (user == null) {
            user = new User();
            user.setUserid(null);
            user.setUsername(EmojiFilter.filterEmoji(userInfo.getNickName()));
            user.setOpenid(openId);
            user.setAvatarUrl(userInfo.getAvatarUrl());
            user.setNickName(EmojiFilter.filterEmoji(userInfo.getNickName()));
            user.setStatus(0);
            user.setCity(userInfo.getCity());
            user.setCountry(userInfo.getCountry());
            user.setProvince(userInfo.getProvince());
            boolean flag = userService.insert(user);
            if(!flag) {
            	return Result.failResult("用户登录失败，请检查是否授权");
            }
        }else {
            user.setPassword(sessionKey);
            userService.updateById(user);
        }
        
        UserRes userRes = new UserRes();
        
        BeanUtils.copyProperties(user, userRes);
        
        UserToken accessToken = tokenService.createTokenByUserId(user.getUserid()+"");
        accessToken.setSessionKey(sessionKey);
        userRes.setExpire(accessToken.getExpire());
        userRes.setToken(accessToken.getTokenId());
        userRes.setExpireString(DateUtil.timestampToDate(accessToken.getExpire()));
        accessToken.getSessionMap().put(accessToken.getSessionKey(), userRes);
        
        return Result.successResult(userRes);
    }
    
	@ApiOperation(value="检测用户登录状态" , notes="" , response= UserRes.class , httpMethod = "POST")
	@PostMapping(value = "checkToken")
	public Result checkToken(@RequestBody LogoutUserReq body) {
		
		if(Utils.isEmpty(body.getToken(),body.getUserid())) {
			return Result.paramInvalidResult("参数不能为空");
		}
		
		UserToken userToken = tokenService.checkToken(body.getToken());
		if(userToken!=null) {
			UserRes userRes = (UserRes) userToken.getSessionMap().get(userToken.getSessionKey());
			userRes.setExpireString(DateUtil.timestampToDate(userToken.getExpire()));
			return Result.successResult(userRes);
		}
		return Result.tokenInvalidResult();
	}


	@ApiOperation(value="用户绑定手机号码" , notes="" , response= UserRes.class , httpMethod = "POST")
    @PostMapping("bindPhone")
    public Object bindPhone(@RequestBody BindPhoneReq body) {
    		if(Utils.isEmpty(body.getToken(),body.getUserid(),body.getEncryptedData(),body.getIv())) {
			return Result.paramInvalidResult("参数不能为空");
		}
    		UserToken userToken = tokenService.checkToken(body.getToken());
    		if(userToken==null) { 
    			return Result.tokenInvalidResult(); 
    		}
    	
        WxMaPhoneNumberInfo phoneNumberInfo = this.wxService.getUserService().getPhoneNoInfo(
        		userToken.getSessionKey(), body.getEncryptedData(), body.getIv());
        String phone = phoneNumberInfo.getPhoneNumber();
        if(Utils.isEmpty(phone)) {
        		return Result.failResult("未获取到手机号码");
        }
        User user = userService.selectById(userToken.getUserId());
        if(Utils.isEmpty(user)){
            return Result.failResult("用户不存在");
        }
        user.setMobile(phone);
        if (userService.updateById(user)) {
            return Result.successResult(user);
        }
        return Result.successResult(user);
    }

	@ApiOperation(value="用户登录注销" , notes="" , response= Result.class , httpMethod = "POST")
    @PostMapping("logout")
	public Result logout(@RequestBody LogoutUserReq body) {
		if(Utils.isEmpty(body.getToken(),body.getUserid())) {
			return Result.failResult("参数不能为空");
		}
		tokenService.removeToken(body.getToken());
		return Result.successResult("注销成功");
	}
}
