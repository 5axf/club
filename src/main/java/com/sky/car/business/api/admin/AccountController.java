package com.sky.car.business.api.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sky.car.business.service.ShutdownContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.admin.Admin;
import com.sky.car.business.entity.admin.AdminInfoRes;
import com.sky.car.business.entity.admin.AdminRes;
import com.sky.car.business.entity.admin.DeleteAdminReq;
import com.sky.car.business.entity.admin.FindAdminReq;
import com.sky.car.business.entity.admin.LoginAdminReq;
import com.sky.car.business.entity.admin.LogoutAdminReq;
import com.sky.car.business.entity.admin.SaveAdminReq;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.account.AdminService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.util.PageUtils;
import com.sky.car.util.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin/account/")
@Api(tags = { "后台管理-管理员接口" })
public class AccountController {

	@Autowired  AdminService adminService;
	
	@Autowired  AdminTokenService adminTokenService;
	
	@Autowired ShutdownContext shutdownContext ;
	


	@ApiOperation(value="查询管理员列表" , notes="" , response= Result.class , httpMethod = "POST")
	@RequestMapping(value = "findAdmins", method = RequestMethod.POST)
	public Result findAdmins(@RequestBody FindAdminReq body) {
		
		AdminToken adminToken = adminTokenService.checkToken(body.getToken());
		if(Utils.isEmpty(adminToken)) {
			return Result.tokenInvalidResult();
		}
		
		Wrapper<Admin> wrapper = new EntityWrapper<>();
		if(Utils.isNotEmpty(body.getAccount())) {
			wrapper.eq("account", body.getAccount());
		}
		if(Utils.isNotEmpty(body.getName())) {
			wrapper.like("name", body.getName());
		}
		if(Utils.isNotEmpty(body.getStatus())) {
			wrapper.eq("status", body.getStatus());
		}
		
		Page<Admin> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
		
		page = adminService.selectPage(page, wrapper);
		
		Page<AdminInfoRes> pageRes = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize()); 
		if(Utils.isNotEmpty(page.getRecords())) {
			pageRes.setCurrent(page.getCurrent());
			pageRes.setSize(page.getSize());
			pageRes.setTotal(page.getTotal());
			List<AdminInfoRes> adminResList = new ArrayList<>();
			AdminInfoRes adminRes = null;
			for(Admin admin : page.getRecords()) {
				adminRes = new AdminInfoRes();
				adminRes.setId(admin.getId());
				adminRes.setAccount(admin.getAccount());
				adminRes.setName(admin.getName());
				adminResList.add(adminRes);
				}
			pageRes.setRecords(adminResList);
			}
		return Result.successResult(pageRes);
	}
	
	@ApiOperation(value="管理员登录" , notes="" , response= AdminRes.class , httpMethod = "POST")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public Result login(@RequestBody LoginAdminReq loginAdmin) {
		
		if(Utils.isEmpty(loginAdmin.getAccount(),loginAdmin.getPassword())) {
			return Result.failResult("参数不能为空");
		}
		Wrapper<Admin> wrapper = new EntityWrapper<>();
		wrapper.eq("account", loginAdmin.getAccount());
		wrapper.eq("password", loginAdmin.getPassword());
		Admin admin = adminService.selectOne(wrapper);
		if (admin != null) {
			AdminToken adminToken = adminTokenService.createTokenByUserId(admin.getId().toString());
			AdminRes res = new AdminRes();
			res.setAccount(loginAdmin.getAccount());
			res.setName(admin.getName());
			res.setToken(adminToken.getTokenId());
			res.setExpire(adminToken.getExpire()+"");
			//保存管理员信息到session
			adminToken.getSessionMap().put(adminToken.getSessionKey(), res);
			return Result.successResult(res);
		}
		return Result.failResult("登录失败");
	}
	
	@ApiOperation(value="检测管理员登录状态" , notes="" , response= AdminRes.class , httpMethod = "POST")
	@RequestMapping(value = "checkToken", method = RequestMethod.POST)
	public Result checkToken(@RequestBody LogoutAdminReq tokenAdmin) {
		
		if(Utils.isEmpty(tokenAdmin.getAccount(),tokenAdmin.getToken())) {
			return Result.failResult("参数不能为空");
		}
		
		AdminToken adminToken = adminTokenService.checkToken(tokenAdmin.getToken());
		if(Utils.isEmpty(adminToken)) {
			AdminRes admin = (AdminRes) adminToken.getSessionMap().get(adminToken.getSessionKey());
			return Result.successResult(admin);
		}
		return Result.tokenInvalidResult();
	}
	
	
	@ApiOperation(value="管理员登录注销" , notes="" , response= Result.class , httpMethod = "POST")
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public Result logout(@RequestBody LogoutAdminReq logoutAdmin) {
		if(Utils.isEmpty(logoutAdmin.getAccount(),logoutAdmin.getToken())) {
			return Result.failResult("参数不能为空");
		}
		adminTokenService.removeToken(logoutAdmin.getToken());
		return Result.successResult("注销成功");
	}
	
	@ApiOperation(value="添加管理员" , notes="" , response= Result.class , httpMethod = "POST")
	@RequestMapping(value = "saveAdmin", method = RequestMethod.POST)
	public Result saveAdmin(@RequestBody SaveAdminReq body) {
		if(Utils.isEmpty(body.getAccount(),body.getPassword(),body.getToken())) {
			return Result.failResult("参数不能为空");
		}
		
		AdminToken adminToken = adminTokenService.checkToken(body.getToken());
		if(adminToken==null) {
			return Result.tokenInvalidResult();
		}
		Wrapper<Admin> wrapper = new EntityWrapper<>();
		wrapper.eq("account", body.getAccount());
		
		List<Admin> list = adminService.selectList(wrapper);
		
		if(Utils.isNotEmpty(list)) {
			return Result.failResult("账号已经存在");
		}
		
		Admin admin = new Admin();
		
		admin.setId(null);
		admin.setAccount(body.getAccount());
		admin.setPassword(body.getPassword());
		admin.setName(body.getName());
		admin.setStatus(1);
		admin.setCreateTime(new Date());
		admin.setUpdateTime(new Date());
		
		boolean flag = adminService.insert(admin);
		
		if(!flag) {
			return Result.failResult("添加管理员失败");
		}
		
		return Result.successResult("添加管理员失败");
	}
	
	@ApiOperation(value="删除管理员" , notes="" , response= Result.class , httpMethod = "POST")
	@RequestMapping(value = "deleteAdmin", method = RequestMethod.POST)
	public Result deleteAdmin(@RequestBody DeleteAdminReq body) {
		
		if(Utils.isEmpty(body.getId(),body.getToken())) {
			return Result.paramInvalidResult();
		}
		
		AdminToken adminToken = adminTokenService.checkToken(body.getToken());
		
		if(adminToken==null) {
			return Result.tokenInvalidResult();
		}
		
		Admin admin = adminService.selectById(body.getId());
		
		if(admin == null) {
			return Result.failResult("管理员不存在");
		}
		
		if(admin.getAccount().equals("admin")) {
			return Result.failResult("超级管理员不允许删除");
		}
		
		if(adminToken.getUserId().equals(admin.getId().toString())) {
			return Result.failResult("管理员不允许删除自己");
		}
		
		boolean flag = adminService.deleteById(body.getId());
		if(!flag) {
			return Result.failResult("删除管理员失败");
		}
		return Result.successResult("删除管理员成功");
	}

}
