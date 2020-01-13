package com.sky.car.business.api.wx;


import com.sky.car.business.entity.userApplication.UserApplicationReq;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.business.service.userApplication.UserApplicationService;
import com.sky.car.common.Result;
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

@RestController
@RequestMapping("/wx/userApplication/")
@Api(tags = { "会员申请管理接口" })
public class WxUserApplicationController {

	@Autowired
	private UserApplicationService userApplicationService;
	@Autowired
	private UserService userService;
	@Autowired
	UserTokenService userTokenService;

	@ApiOperation(value="用户申请会员" , notes="用户申请会员", response= Result.class , httpMethod = "POST")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "token",value = "登录标识",required = true),
			@ApiImplicitParam(name = "userId",value = "用户id",required = true),
			@ApiImplicitParam(name = "realName",value = "真实姓名",required = true),
			@ApiImplicitParam(name = "mobile",value = "手机号码",required = true),
			@ApiImplicitParam(name = "birthday",value = "真实姓名",required = true),
	})
	@RequestMapping(value = "addUserApplication", method = RequestMethod.POST)
	public Result addUserApplication(@RequestBody UserApplicationReq body){
//		if(Utils.isEmpty(body.getToken())) {
//			return Result.failResult("token为空");
//		}
//		UserToken userToken = userTokenService.checkToken(body.getToken());
//		if (null == userToken) {
//			return Result.tokenInvalidResult();
//		}
		if(Utils.isEmpty(body.getUserId())){
			return Result.failResult("参数userId不能为空");
		}
		if(Utils.isEmpty(body.getRealName())){
			return Result.failResult("参数realName不能为空");
		}
		if(Utils.isEmpty(body.getMobile())){
			return Result.failResult("参数mobile不能为空");
		}
//		if(Utils.isEmpty(body.getBirthday())){
//			return Result.failResult("参数birthday不能为空");
//		}
		return userApplicationService.addUserApplication(body);
	}

}
