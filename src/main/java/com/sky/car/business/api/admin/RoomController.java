package com.sky.car.business.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.entity.room.RoomReq;
import com.sky.car.business.entity.room.RoomRes;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.room.RoomService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.util.PageUtils;
import com.sky.car.util.Utils;
import io.swagger.annotations.*;
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
@RequestMapping("/admin/room/")
@Api(tags = { "后台管理-房间管理接口" })
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    AdminTokenService adminTokenService;

    @ApiOperation(value="查询房间列表" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "roomName",value = "房间名称",required = false),
    })
    @RequestMapping(value = "queryRoom", method = RequestMethod.POST)
    public Result queryRoom(@RequestBody RoomReq body) {
        Wrapper<Room> wrapper = new EntityWrapper<>();
        if(Utils.isNotEmpty(body.getRoomName())) {
            wrapper.eq("roomName", body.getRoomName());
        }
        wrapper.eq("isDel", 1);
        wrapper.orderBy("createTime", false);
        Page<Room> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        page = roomService.selectPage(page, wrapper);
        Page<RoomRes> pageRes = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        if(Utils.isNotEmpty(page.getRecords())) {
            pageRes.setCurrent(page.getCurrent());
            pageRes.setSize(page.getSize());
            pageRes.setTotal(page.getTotal());
            List<RoomRes> roomResList = new ArrayList<>();
            RoomRes roomRes = null;
            for(Room room : page.getRecords()) {
                roomRes = new RoomRes();
                BeanUtils.copyProperties(room, roomRes);
                roomResList.add(roomRes);
            }
            pageRes.setRecords(roomResList);
        }
        return Result.successResult(pageRes);
    }

    @ApiOperation(value="添加房间" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "roomName",value = "房间名称",required = true),
            @ApiImplicitParam(name = "roomDes",value = "房间描述",required = true),
            @ApiImplicitParam(name = "roomImg",value = "房间图片",required = true),
    })
    @RequestMapping(value = "addRoom", method = RequestMethod.POST)
    public Result addRoom(@RequestBody RoomReq body) {

        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(adminToken)) {
            return Result.tokenInvalidResult();
        }
        if (Utils.isEmpty(body.getRoomName())) {
            return Result.failResult("房间名称不能为空");
        }
        if (Utils.isEmpty(body.getRoomDes())) {
            return Result.failResult("房间描述不能为空");
        }
        Room room = new Room();
        BeanUtils.copyProperties(body, room);
        room.setIsFrame(1);
        room.setIsDel(1);
        room.setCreateTime(new Date());
        boolean insert = roomService.insert(room);
        if(insert){
            return Result.successResult("房间添加成功");
        }else {
            return Result.failResult("房间添加失败");
        }
    }

    @ApiOperation(value="修改房间" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "roomId",value = "房间id",required = true),
            @ApiImplicitParam(name = "roomName",value = "房间名称",required = true),
            @ApiImplicitParam(name = "roomDes",value = "房间描述",required = true),
            @ApiImplicitParam(name = "roomImg",value = "房间图片",required = true),
    })
    @RequestMapping(value = "updateRoom", method = RequestMethod.POST)
    public Result updateRoom(@RequestBody RoomReq body) {

        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(adminToken)) {
            return Result.tokenInvalidResult();
        }
        if (Utils.isEmpty(body.getRoomId())) {
            return Result.failResult("房间id不能为空");
        }
        if (Utils.isEmpty(body.getRoomName())) {
            return Result.failResult("房间名称不能为空");
        }
        if (Utils.isEmpty(body.getRoomDes())) {
            return Result.failResult("房间描述不能为空");
        }
        if (Utils.isEmpty(body.getRoomImg())) {
            return Result.failResult("房间图片不能为空");
        }
        Room room1 = roomService.selectById(body.getRoomId());
        if(Utils.isEmpty(room1)){
            return Result.failResult("房间不存在");
        }
        BeanUtils.copyProperties(body, room1);
        room1.setUpdateTime(new Date());
        boolean update = roomService.updateById(room1);
        if(update){
            return Result.successResult("房间修改成功");
        }else {
            return Result.failResult("房间修改失败");
        }
    }

    @ApiOperation(value="删除房间" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "roomId",value = "房间id",required = true),
    })
    @RequestMapping(value = "deleteRoom", method = RequestMethod.POST)
    public Result deleteRoom(@RequestBody RoomReq body) {

        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(adminToken)) {
            return Result.tokenInvalidResult();
        }
        if (Utils.isEmpty(body.getRoomId())) {
            return Result.failResult("房间id不能为空");
        }
        Room room1 = roomService.selectById(body.getRoomId());
        if(Utils.isEmpty(room1)){
            return Result.failResult("房间不存在");
        }
        //逻辑删除
        room1.setIsDel(2);
        room1.setUpdateTime(new Date());
        boolean update = roomService.updateById(room1);
        if(update){
            return Result.successResult("房间删除成功");
        }else {
            return Result.failResult("房间删除失败");
        }
    }

    @ApiOperation(value="更改房间状态" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "roomId",value = "房间id",required = true),
            @ApiImplicitParam(name = "isFrame",value = "开放关闭（1表示开发，2表示关闭）",required = true),
    })
    @RequestMapping(value = "updateRoomStatus", method = RequestMethod.POST)
    public Result updateRoomStatus(@RequestBody RoomReq body) {

        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(adminToken)) {
            return Result.tokenInvalidResult();
        }
        if (Utils.isEmpty(body.getIsFrame())) {
            return Result.failResult("房间状态码不能为空");
        }
        if (Utils.isEmpty(body.getRoomId())) {
            return Result.failResult("房间id不能为空");
        }
        Room room1 = roomService.selectById(body.getRoomId());
        if(Utils.isEmpty(room1)){
            return Result.failResult("房间不存在");
        }
        room1.setIsFrame(body.getIsFrame());
        room1.setUpdateTime(new Date());
        boolean update = roomService.updateById(room1);
        if(update){
            return Result.successResult("房间状态更改成功");
        }else {
            return Result.failResult("房间状态更改失败");
        }
    }

}
