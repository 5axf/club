package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.hours.HoursReq;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.hours.HoursService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
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

import java.util.List;

@RestController
@RequestMapping("/wx/hours/")
@Api(tags = { "时间段管理接口" })
public class WxHoursController {

    @Autowired
    private HoursService hoursService;

    @Autowired
    UserTokenService userTokenService;

    @ApiOperation(value="查询时间段列表" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
    })
    @RequestMapping(value = "queryhours", method = RequestMethod.POST)
    public Result queryhours(@RequestBody HoursReq body) {
        Wrapper<Hours> wrapper = new EntityWrapper<>();
        wrapper.eq("isView",1);
        List<Hours> hours = hoursService.selectList(wrapper);
        if (Utils.isEmpty(hours)){
            return Result.failResult("数据为空");
        }
        return Result.successResult(hours);
    }
}
