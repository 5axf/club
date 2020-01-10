package com.sky.car.business.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.hours.HoursReq;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.entity.room.RoomReq;
import com.sky.car.business.entity.room.RoomRes;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.hours.HoursService;
import com.sky.car.business.service.room.RoomService;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/hours/")
@Api(tags = { "后台管理-时间段管理接口" })
public class HoursController {

    @Autowired
    private HoursService hoursService;

    @Autowired
    AdminTokenService adminTokenService;

    @ApiOperation(value="查询时间段列表" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "hoursName",value = "时间段名称",required = false),
    })
    @RequestMapping(value = "queryhours", method = RequestMethod.POST)
    public Result queryhours(@RequestBody HoursReq body) {

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        Wrapper<Hours> wrapper = new EntityWrapper<>();
        if(Utils.isNotEmpty(body.getHoursName())){
            wrapper.like("hoursName", body.getHoursName());
        }
        List<Hours> hours = hoursService.selectList(wrapper);
        if(Utils.isEmpty(hours)){
            return Result.failResult("暂无数据");
        }else {
            return Result.successResult(hours);
        }
    }

    @ApiOperation(value="添加时间段" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "hoursName",value = "时间段名称",required = true),
            @ApiImplicitParam(name = "hourseStartTime",value = "时间段开始时间",required = true),
            @ApiImplicitParam(name = "hourseEndTime",value = "时间段结束时间",required = true),
    })
    @RequestMapping(value = "addHours", method = RequestMethod.POST)
    public Result addHours(@RequestBody HoursReq body) {

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if (Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if (Utils.isEmpty(body.getHoursName())) {
            return Result.failResult("时间段名称不能为空");
        }
        if (Utils.isEmpty(body.getHourseStartTime())) {
            return Result.failResult("时间段开始时间不能为空");
        }
        if (Utils.isEmpty(body.getHourseEndTime())) {
            return Result.failResult("时间段结束时间不能为空");
        }
        Hours hours = new Hours();
        BeanUtils.copyProperties(body, hours);
        hours.setIsView(1);
        boolean insert = hoursService.insert(hours);
        if(insert){
            return Result.successResult("时间段添加成功");
        }else {
            return Result.failResult("时间段添加失败");
        }
    }

    @ApiOperation(value="修改时间段" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "hoursId",value = "时间段id",required = true),
            @ApiImplicitParam(name = "hoursName",value = "时间段名称",required = true),
            @ApiImplicitParam(name = "hourseStartTime",value = "时间段开始时间",required = true),
            @ApiImplicitParam(name = "hourseEndTime",value = "时间段结束时间",required = true),
    })
    @RequestMapping(value = "updateHours", method = RequestMethod.POST)
    public Result updateHours(@RequestBody HoursReq body) {

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if (Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if (Utils.isEmpty(body.getHoursId())) {
            return Result.failResult("时间段id不能为空");
        }
        if (Utils.isEmpty(body.getHoursName())) {
            return Result.failResult("时间段名称不能为空");
        }
        if (Utils.isEmpty(body.getHourseStartTime())) {
            return Result.failResult("时间段开始时间不能为空");
        }
        if (Utils.isEmpty(body.getHourseEndTime())) {
            return Result.failResult("时间段结束时间不能为空");
        }
        Hours hours1 = hoursService.selectById(body.getHoursId());
        if(Utils.isEmpty(hours1)){
            return Result.failResult("数据不存在");
        }
        BeanUtils.copyProperties(body, hours1);
        boolean update = hoursService.updateById(hours1);
        if(update){
            return Result.successResult("时间段修改成功");
        }else {
            return Result.failResult("时间段修改失败");
        }
    }

    @ApiOperation(value="删除时间段" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "hoursId",value = "时间段id",required = true),
    })
    @RequestMapping(value = "deleteHours", method = RequestMethod.POST)
    public Result deleteHours(@RequestBody HoursReq body) {

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if (Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if (Utils.isEmpty(body.getHoursId())) {
            return Result.failResult("时间段id不能为空");
        }
        Hours hours1 = hoursService.selectById(body.getHoursId());
        if(Utils.isEmpty(hours1)){
            return Result.failResult("数据不存在");
        }
        boolean b = hoursService.deleteById(hours1.getHoursId());
        if(b){
            return Result.successResult("时间段删除成功");
        }else {
            return Result.failResult("时间段删除失败");
        }
    }
}
