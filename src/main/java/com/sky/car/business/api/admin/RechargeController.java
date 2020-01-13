package com.sky.car.business.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.recharge.Recharge;
import com.sky.car.business.entity.recharge.RechargeReq;
import com.sky.car.business.entity.recharge.RechargeRes;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.recharge.RechargeService;
import com.sky.car.business.service.user.UserService;
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
import java.util.List;

@RestController
@RequestMapping("/admin/recharge/")
@Api(tags = { "后台管理-充值记录管理接口" })
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private UserService userService;
    @Autowired
    AdminTokenService adminTokenService;

    @ApiOperation(value="查询充值记录列表" , notes="查询充值记录列表" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "outTradeNo",value = "充值订单号",required = false),
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false),
    })
    @RequestMapping(value = "queryRechargeList", method = RequestMethod.POST)
    public Result queryRechargeList(@RequestBody RechargeReq body) {

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        Wrapper<Recharge> wrapper = new EntityWrapper<>();

        if(Utils.isNotEmpty(body.getOutTradeNo())){
            wrapper.eq("outTradeNo", body.getOutTradeNo());
        }
        if(Utils.isNotEmpty(body.getStartTime())){
            wrapper.ge("createTime", body.getStartTime());
        }
        if(Utils.isNotEmpty(body.getEndTime())){
            wrapper.le("createTime", body.getEndTime());
        }

        wrapper.orderBy("createTime", false);
        Page<Recharge> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        page = rechargeService.selectPage(page, wrapper);
        Page<RechargeRes> pageRes = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        if(Utils.isNotEmpty(page.getRecords())) {
            pageRes.setCurrent(page.getCurrent());
            pageRes.setSize(page.getSize());
            pageRes.setTotal(page.getTotal());
            List<RechargeRes> rechargeResList = new ArrayList<>();
            RechargeRes rechargeRes = null;
            for(Recharge recharge : page.getRecords()) {
                rechargeRes = new RechargeRes();
                BeanUtils.copyProperties(recharge, rechargeRes);
                User user = userService.selectById(recharge.getUserId());
                if(Utils.isNotEmpty(user)){
                    rechargeRes.setUser(user);
                }
                rechargeResList.add(rechargeRes);
            }
            pageRes.setRecords(rechargeResList);
        }
        return Result.successResult(pageRes);
    }

}
