package com.sky.car.business.api.wx;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.Wrapper;

@RestController
@RequestMapping("/wx/user/")
@Validated
@Api(tags = { "小程序-用户接口" })
public class WxUserController {
    @Autowired UserService userService;

    @Autowired UserTokenService userTokenService;



    @ApiOperation(value="更新用户信息" , notes="修改头像/真实姓名/电话号码" ,
    response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    public Result updateUserInfo(@RequestBody User body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (null == userToken) {
            return Result.tokenInvalidResult();
        }

        Wrapper<User> wrapper = new EntityWrapper<User>();
        wrapper.eq("userid", body.getUserid());
        User user = userService.selectOne(wrapper);
        if(Utils.isEmpty(user)){
            return Result.failResult("用户信息错误!");
        }
        if(Utils.isNotEmpty(body.getAvatarUrl())){//更换头像
            user.setAvatarUrl(body.getAvatarUrl());
        }
        if(Utils.isNotEmpty(body.getRealName())){//更换真实姓名
            user.setRealName(body.getRealName());
        }
        if(Utils.isNotEmpty(body.getMobile())){//更换手机号
            user.setMobile(body.getMobile());
        }
        if(Utils.isNotEmpty(body.getBirthday())){
            user.setBirthday(body.getBirthday());
        }

        boolean flag = userService.update(user, wrapper);
        if(flag){
            return Result.successResult("用户信息更新成功!");
        } else {
            return Result.failResult("用户信息更新失败!");
        }
    }

    @ApiOperation(value="获取用户信息" , notes="获取用户信息以供展示" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.POST)
    public Result getUserInfo(@RequestBody User body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (null == userToken) {
            return Result.tokenInvalidResult();
        }

        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("userid", userToken.getUserId());

        User user = userService.selectOne(wrapper);
        if(Utils.isEmpty(user)){
            return Result.failResult("用户信息错误!");
        }

        return Result.successResult("获取用户信息成功!", user);
    }

    @ApiOperation(value="修改头像" , notes="" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "updateUserRes", method = RequestMethod.POST)
    public Result updateUserRes(@RequestBody User body){
        if(Utils.isEmpty(body.getToken(),body.getAvatarUrl())) {
            return Result.failResult("参数不能为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        // 更新用户
        User user = userService.selectById(userToken.getUserId());
        user.setAvatarUrl(body.getAvatarUrl());
        userService.updateById(user);

        return Result.successResult();
    }

}
