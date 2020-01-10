package com.sky.car.business.api.admin;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.user.FindUserReq;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.Result;
import com.sky.car.util.PageUtils;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/admin/user/")
@Api(tags = { "后台管理-用户管理接口" })
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired AdminTokenService adminTokenService;


	@ApiOperation(value="查询用户列表" , notes="" , response= Result.class , httpMethod = "POST")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token",value = "登录标识",required = true),
			@ApiImplicitParam(name = "userName",value = "用户姓名",required = false ),
			@ApiImplicitParam(name = "cardNo",value = "会员卡号",required = false),
			@ApiImplicitParam(name = "mobile",value = "手机号码",required = false),
	})
	@RequestMapping(value = "queryUserlist", method = RequestMethod.POST)
	public Result queryUserlist(@RequestBody FindUserReq body) {
		
//		AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//
//		if(adminToken==null) {
//			return Result.tokenInvalidResult();
//		}
		Wrapper<User> wrapper = new EntityWrapper<>();
		if(Utils.isNotEmpty(body.getUserName())) {
			//用户真实姓名
			wrapper.like("realName", body.getUserName());
		}
		if(Utils.isNotEmpty(body.getCardNo())){
			wrapper.eq("cardNo", body.getCardNo());
		}
		if(Utils.isNotEmpty(body.getType())){
			wrapper.eq("type", body.getType());
		}else {
			//默认查询会员用户
			wrapper.eq("type", 2);
		}
		if(Utils.isNotEmpty(body.getMobile())){
			wrapper.eq("mobile", body.getMobile());
		}
		
		//按照创建时间靠前面 
		Set<String> setOrder = new HashSet<>();
		setOrder.add("createTime");
		setOrder.add("userid");
		wrapper.orderDesc(setOrder);
		
		Page<User> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
		page = userService.selectPage(page, wrapper);
		return Result.successResult(page);
	}
}
