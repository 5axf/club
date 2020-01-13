/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sky.car.business.service.userApplication;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.entity.userApplication.UserApplication;
import com.sky.car.business.entity.userApplication.UserApplicationReq;
import com.sky.car.business.mapper.userApplication.UserApplicationMapper;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.Result;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import com.sky.car.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Random;

@Service
public class UserApplicationService extends BaseService<UserApplicationMapper, UserApplication> {
	
    @Autowired
    private UserApplicationMapper userApplicationMapper;

	@Override
	public BaseMapper<UserApplication> getDao() {
		return userApplicationMapper;
	}


	@Autowired
	private UserApplicationService userApplicationService;

	@Autowired
	AdminTokenService adminTokenService;
	@Autowired
	private UserService userService;

	//审核
	@Transactional
	public Result updateStatus(@RequestBody UserApplicationReq body){
		UserApplication userApplication = userApplicationService.selectById(body.getUserApplicationId());
		if(Utils.isEmpty(userApplication)){
			return Result.failResult("数据不存在");
		}
		User user = userService.selectById(userApplication.getUserId());
		if(Utils.isEmpty(user)){
			return Result.failResult("用户不存在");
		}
		boolean flag = false;
		userApplication.setUpdateTime(new Date());
		userApplication.setStatus(body.getStatus());
		flag = userApplicationService.updateById(userApplication);

		if(body.getStatus() == 2){
			//用户更改为会员状态
			user.setStatus(4);
			user.setType("2");
			flag = userService.updateById(user);
		}
		if(flag){
			return Result.successResult("审核成功");
		}else {
			return Result.failResult("审核失败");
		}
	}

	//添加会员申请
	@Transactional
	public Result addUserApplication(@RequestBody UserApplicationReq body){
		User user = userService.selectById(body.getUserId());
		if(Utils.isEmpty(user)){
			return Result.failResult("用户不存在");
		}

		Wrapper<UserApplication> wrapper = new EntityWrapper<>();
		wrapper.eq("userId", body.getUserId());
		UserApplication userApplication1 = userApplicationService.selectOne(wrapper);
		if(Utils.isNotEmpty(userApplication1)){
			if(userApplication1.getStatus() == 1){
				return Result.failResult("申请审核中，请勿重复提交");
			}else if(userApplication1.getStatus() == 2){
				return Result.failResult("已是会员");
			}
		}

		boolean flag = false;
		user.setStatus(3);//审核中
		user.setRealName(body.getRealName());
		user.setBirthday(body.getBirthday());
		user.setMobile(body.getMobile());
		user.setCardNo(getCardNo());
		user.setUpdateTime(new Date());
		flag = userService.updateById(user);

		UserApplication userApplication = new UserApplication();
		userApplication.setUserId(user.getUserid());
		userApplication.setStatus(1);//审核中
		userApplication.setCreateTime(new Date());
		flag = userApplicationService.insert(userApplication);

		if(flag){
			return Result.successResult("申请提交成功，等待管理员审核");
		}else {
			return Result.failResult("申请提交失败，请稍后再试");
		}
	}

	/**
	 * 获取四位随机数
	 * @return
	 */
	public String getCardNo(){
		Random random = new Random();
		String str  = (random.nextInt(9000))+1000 +"";
		while (str.contains("4")){
			str  = (random.nextInt(9000))+1000 +"";
		}
		return "VIP"+str;
	}

	public static void main(String[] args) {

//		UUID uuid = UUID.randomUUID();
//		System.out.printf("=======>:"+uuid);
		while (true){
			try {
				Thread.currentThread().sleep(1000);
				Random random = new Random();
				String str  = (random.nextInt(9000))+1000 +"";
				while (str.contains("4")){
					str  = (random.nextInt(9000))+1000 +"";
				}
				System.out.println("===>:"+str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}





}
