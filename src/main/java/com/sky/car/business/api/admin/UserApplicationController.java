package com.sky.car.business.api.admin;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.user.FindUserReq;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.entity.userApplication.UserApplication;
import com.sky.car.business.entity.userApplication.UserApplicationReq;
import com.sky.car.business.entity.userApplication.UserApplicationRes;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.business.service.userApplication.UserApplicationService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.util.PageUtils;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/userApplication/")
@Api(tags = { "后台管理-会员申请管理接口" })
public class UserApplicationController {

	@Autowired
	private UserApplicationService userApplicationService;
	
	@Autowired AdminTokenService adminTokenService;
	@Autowired
	private UserService userService;


	@ApiOperation(value="查询用户申请会员列表" , notes="查询用户申请会员列表", response= Result.class , httpMethod = "POST")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token",value = "登录标识",required = true),
			@ApiImplicitParam(name = "status",value = "审核状态",required = false),
	})
	@RequestMapping(value = "queryUserApplicationList", method = RequestMethod.POST)
	public Result queryUserApplicationList(@RequestBody UserApplicationReq body) {
//		AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//		if(adminToken==null) {
//			return Result.tokenInvalidResult();
//		}
		Wrapper<UserApplication> wrapper = new EntityWrapper<>();
		if(Utils.isNotEmpty(body.getStatus())){
			wrapper.eq("status", body.getStatus());
		}
		//按照创建时间靠前面 
		Set<String> setOrder = new HashSet<>();
		setOrder.add("createTime");
		wrapper.orderDesc(setOrder);

		Page<UserApplication> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
		page = userApplicationService.selectPage(page, wrapper);
		List<UserApplication> userApplications = page.getRecords();
		if(Utils.isEmpty(userApplications)){
			return Result.failResult("暂无数据");
		}
		Page<UserApplicationRes> pageRes = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
		pageRes.setCurrent(page.getCurrent());
		pageRes.setSize(page.getSize());
		pageRes.setTotal(page.getTotal());
		List<UserApplicationRes> list = new ArrayList<>();
		UserApplicationRes res = null;
		for (UserApplication userApplication : userApplications){
			res = new UserApplicationRes();
			BeanUtils.copyProperties(userApplication, res);
			User user = userService.selectById(userApplication.getUserId());
			res.setUser(user);
			list.add(res);
		}
		pageRes.setRecords(list);
		return Result.successResult(pageRes);
	}

	@ApiOperation(value="审核用户申请" , notes="审核用户申请", response= Result.class , httpMethod = "POST")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token",value = "登录标识",required = true),
			@ApiImplicitParam(name = "userApplicationId",value = "用户申请记录id",required = true),
			@ApiImplicitParam(name = "status",value = "审核状态：1、审核中；2、审核通过；3审核不通过",required = true),
	})
	@RequestMapping(value = "updateStatus", method = RequestMethod.POST)
	public Result updateStatus(@RequestBody UserApplicationReq body){
//		AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//		if(adminToken==null) {
//			return Result.tokenInvalidResult();
//		}
		if(Utils.isEmpty(body.getUserApplicationId())){
			return Result.failResult("参数userApplicationId不能为空");
		}
		if(Utils.isEmpty(body.getStatus())){
			return Result.failResult("参数status不能为空");
		}
		return userApplicationService.updateStatus(body);
	}

}
