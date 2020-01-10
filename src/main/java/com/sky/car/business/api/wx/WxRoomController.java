package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.entity.room.RoomReq;
import com.sky.car.business.entity.room.RoomRes;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.room.RoomService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
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
@RequestMapping("/wx/room/")
@Api(tags = { "后台管理-房间管理接口" })
public class WxRoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    UserTokenService userTokenService;

    @ApiOperation(value="查询房间列表" , notes="" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
    })
    @RequestMapping(value = "queryRoomList", method = RequestMethod.POST)
    public Result queryRoomList(@RequestBody RoomReq body) {
        Wrapper<Room> wrapper = new EntityWrapper<>();
        wrapper.eq("isDel",1);
        wrapper.eq("isFrame",1);
        List<Room> rooms = roomService.selectList(wrapper);
        if (Utils.isEmpty(rooms)){
            return Result.failResult("暂无房间");
        }
        return Result.successResult(rooms);
    }

}
