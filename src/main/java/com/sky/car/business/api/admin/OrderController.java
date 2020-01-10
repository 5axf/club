package com.sky.car.business.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.order.*;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.order.OrderDishService;
import com.sky.car.business.service.order.OrderService;
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

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/order/")
@Api(tags = { "后台管理-订单管理接口" })
public class OrderController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminTokenService adminTokenService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDishService orderDishService;

    @ApiOperation(value="查询订单列表" , notes="查询订单列表" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderNum",value = "订单号",required = false),
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false),
    })
    @RequestMapping(value = "queryOrderList", method = RequestMethod.POST)
    public Result queryOrderList(@RequestBody OrderReq body){

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        Wrapper<Order> wrapper = new EntityWrapper<>();

        if(Utils.isNotEmpty(body.getOrderNum())){
            wrapper.eq("orderNum", body.getOrderNum());
        }
        if(Utils.isNotEmpty(body.getStartTime())){
            wrapper.ge("creatTime", body.getStartTime());
        }
        if(Utils.isNotEmpty(body.getEndTime())){
            wrapper.le("creatTime", body.getEndTime());
        }
        if(Utils.isNotEmpty(body.getPhone())){
            wrapper.le("phone", body.getPhone());
        }
        wrapper.orderBy("creatTime", false);
        Page<Order> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        page = orderService.selectPage(page, wrapper);
        Page<OrderRes> pageRes = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        if(Utils.isNotEmpty(page.getRecords())) {
            pageRes.setCurrent(page.getCurrent());
            pageRes.setSize(page.getSize());
            pageRes.setTotal(page.getTotal());
            List<OrderRes> orderResList = new ArrayList<>();
            OrderRes orderRes = null;
            for(Order order : page.getRecords()) {
                orderRes = new OrderRes();
                BeanUtils.copyProperties(order, orderRes);
                Wrapper<OrderDish> wrapper1 = new EntityWrapper<>();
                wrapper1.eq("orderId", order.getOrderId());
                wrapper1.eq("state", 1);//查询有效数据
                List<OrderDish> orderDishes = orderDishService.selectList(wrapper1);
                if(Utils.isNotEmpty(orderDishes)){
                    orderRes.setOrderDishList(orderDishes);
                }
                orderResList.add(orderRes);
            }
            pageRes.setRecords(orderResList);
        }
        return Result.successResult(pageRes);
    }

    @ApiOperation(value="根据订单id查询订单详情" , notes="根据订单id查询订单详情" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderId",value = "订单id",required = true),
    })
    @RequestMapping(value = "queryOrderDetail", method = RequestMethod.POST)
    public Result queryOrderDetail(@RequestBody OrderReq body){

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if(Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数orderId不能为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }

        Wrapper<OrderDish> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("orderId", order.getOrderId());
        wrapper1.eq("state", 1);//查询有效数据
        List<OrderDish> orderDishes = orderDishService.selectList(wrapper1);
        if(Utils.isNotEmpty(orderDishes)){
            return Result.successResult(orderDishes);
        }else {
            return Result.failResult("暂无数据");
        }

    }

    @ApiOperation(value="修改订单" , notes="更改订单状态，人数等" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderId",value = "订单id",required = true),
    })
    @RequestMapping(value = "updateOrderState", method = RequestMethod.POST)
    public Result updateOrder(@RequestBody OrderReq body){

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }

        if(Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数orderId不能为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        if(Utils.isNotEmpty(body.getState())){
            order.setState(body.getState());
        }
        if(Utils.isNotEmpty(body.getUseNum())){
            order.setUseNum(body.getUseNum());
        }
        if(Utils.isNotEmpty(body.getRemark())){
            order.setRemark(body.getRemark());
        }
        if(Utils.isNotEmpty(body.getChildNum())){
            order.setChildNum(body.getChildNum());
        }
        order.setUpdateTime(new Date());
        boolean update = orderService.updateById(order);
        if(update){
            return Result.successResult("修改订单状态成功");
        }else {
            return Result.failResult("修改订单状态失败");
        }
    }

    @ApiOperation(value="根据订单id添加订单详情（加菜，代客点单）" , notes="根据订单id添加订单详情" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderId",value = "订单id",required = true),
            @ApiImplicitParam(name = "orderNum",value = "订单编号",required = true),
            @ApiImplicitParam(name = "dishId",value = "菜品id",required = true),
            @ApiImplicitParam(name = "dishName",value = "菜品名",required = true),
            @ApiImplicitParam(name = "price",value = "菜品单价",required = true),
            @ApiImplicitParam(name = "num",value = "数量",required = true),
    })
    @RequestMapping(value = "addOrderDish", method = RequestMethod.POST)
    public Result addOrderDish(@RequestBody OrderDishReq body){

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if(Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数orderId不能为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        OrderDish orderDish = new OrderDish();
        BeanUtils.copyProperties(body, orderDish);
        orderDish.setState(1);
        orderDish.setCreatTime(new Date());
        boolean insert = orderDishService.insert(orderDish);;
        if(insert){
            return Result.successResult("添加成功");
        }else {
            return Result.failResult("添加失败");
        }
    }

    @ApiOperation(value="根据订单id批量添加订单详情（加菜，代客点单）" , notes="根据订单id批量添加订单详情" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderId",value = "订单id",required = true),
            @ApiImplicitParam(name = "orderDishList",value = "多个菜品信息",required = true),
    })
    @RequestMapping(value = "addOrderDishBatch", method = RequestMethod.POST)
    public Result addOrderDishBatch(@RequestBody OrderDishReq body){

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if(Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数orderId不能为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        if(Utils.isNotEmpty(body.getOrderDishList())){
            boolean b = orderDishService.insertBatch(body.getOrderDishList());
            if(b){
                return Result.successResult("批量添加成功");
            }else {
                return Result.failResult("批量添加失败");
            }
        }
        return Result.failResult("批量添加失败");
    }

    @ApiOperation(value="修改订单（菜品）" , notes="更改订单状态，数量等" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderDishId",value = "订单菜品id",required = true),
    })
    @RequestMapping(value = "updateOrderDish", method = RequestMethod.POST)
    public Result updateOrderDish(@RequestBody OrderDishReq body){

//        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if(Utils.isEmpty(adminToken)) {
//            return Result.tokenInvalidResult();
//        }
        if(Utils.isEmpty(body.getOrderDishId())){
            return Result.failResult("参数orderDishId不能为空");
        }
        OrderDish orderDish = orderDishService.selectById(body.getOrderDishId());
        if(Utils.isEmpty(orderDish)){
            return Result.failResult("订单详情菜品不存在");
        }
        if(Utils.isNotEmpty(body.getState())){
            orderDish.setState(body.getState());
        }
        if(Utils.isNotEmpty(body.getNum())){
            orderDish.setNum(body.getNum());
        }

        orderDish.setUpdateTime(new Date());
        boolean update = orderDishService.updateById(orderDish);
        if(update){
            return Result.successResult("修改订单详情状态成功");
        }else {
            return Result.failResult("修改订单详情状态失败");
        }
    }

}
