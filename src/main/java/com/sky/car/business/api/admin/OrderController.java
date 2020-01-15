package com.sky.car.business.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.order.*;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.food.DishService;
import com.sky.car.business.service.hours.HoursService;
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

import java.util.*;

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
    @Autowired
    private DishService dishService;
    @Autowired
    private HoursService hoursService;

    @ApiOperation(value="查询订单列表" , notes="查询订单列表" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderNum",value = "订单号",required = false),
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false),
    })
    @RequestMapping(value = "queryOrderList", method = RequestMethod.POST)
    public Result queryOrderList(@RequestBody OrderReq body){

        Wrapper<Order> wrapper = new EntityWrapper<>();

        if(Utils.isNotEmpty(body.getOrderNum())){
            wrapper.eq("orderNum", body.getOrderNum());
        }
        if(Utils.isNotEmpty(body.getStartTime())){
            wrapper.ge("creaTime", body.getStartTime());
        }
        if(Utils.isNotEmpty(body.getEndTime())){
            wrapper.le("creaTime", body.getEndTime());
        }
        if(Utils.isNotEmpty(body.getPhone())){
            wrapper.eq("phone", body.getPhone());
        }
        if(Utils.isNotEmpty(body.getState())){
            wrapper.eq("state", body.getState());
        }
        wrapper.ne("state", 7);
        wrapper.orderBy("creaTime", false);
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
                User user = userService.selectById(order.getUserId());
                if (Utils.isEmpty(user.getRealName())){
                    orderRes.setUserName(user.getNickName());
                }else {
                    orderRes.setUserName(user.getRealName());
                    orderRes.setCardNo(user.getCardNo());
                }

                Hours hours = hoursService.selectById(order.getHoursId());
                orderRes.setHourName(hours.getHoursName());
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

        Map<String,Object> returnMap = new HashMap<>();
        if(Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数orderId不能为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        Wrapper<Hours> wrapper = new EntityWrapper<>();
        wrapper.eq("hoursId", order.getHoursId());
        Hours hours = hoursService.selectOne(wrapper);
        returnMap.put("hours",hours);
        returnMap.put("order", order);
        Wrapper<OrderDish> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("orderId", order.getOrderId());
        wrapper1.eq("state", 1);//查询有效数据
        List<OrderDish> orderDishes = orderDishService.selectList(wrapper1);
        if(Utils.isNotEmpty(orderDishes)){
            returnMap.put("orderDishes",orderDishes);
            return Result.successResult(returnMap);
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
    public Result updateOrderState(@RequestBody OrderReq body){

        if(Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数orderId不能为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        if (order.getState() == 6){
            return Result.failResult("订单已结束，无法修改");
        }
        if(Utils.isNotEmpty(body.getState())){
            order.setState(body.getState());
            if(body.getState() == 6){
                order.setPayWey(2);
                order.setPayTime(new Date());
            }
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
        if(Utils.isEmpty(body.getOrderDishList())){
            return Result.failResult("参数orderDishList不能为空");
        }
        Result result = orderDishService.addOrderDishBatch(body);
        if(result.getCode() == 0){
            boolean b = orderDishService.RecalculatedAmount((Order) result.getData());
            if(b){
                return Result.successResult("批量添加成功");
            }else {
                return Result.failResult("批量添加失败");
            }
        }
        return result;
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

    @ApiOperation(value="修改订单（菜品）数量" , notes="更改订单数量" , response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "orderDishId",value = "订单菜品id",required = true),
            @ApiImplicitParam(name = "flag",value = "标识，菜数量加减（1，表示数量加；2，表示数量减）",required = true),
    })
    @RequestMapping(value = "updateOrderDishNum", method = RequestMethod.POST)
    public Result updateOrderDishNum(@RequestBody OrderDishReq body){

        if(Utils.isEmpty(body.getOrderDishId())){
            return Result.failResult("参数orderDishId不能为空");
        }
        OrderDish orderDish = orderDishService.selectById(body.getOrderDishId());
        if(Utils.isEmpty(orderDish)){
            return Result.failResult("订单详情菜品不存在");
        }
        if(1 == body.getFlag()){
            orderDish.setNum(orderDish.getNum() + 1);
        }else if(2 == body.getFlag()){
            orderDish.setNum(orderDish.getNum() - 1);
        }
        orderDish.setUpdateTime(new Date());
        boolean update = orderDishService.updateById(orderDish);
        if(update){
            return Result.successResult("修改订单详情状态成功");
        }else {
            return Result.failResult("修改订单详情状态失败");
        }
    }

    @ApiOperation(value="服务员再次下单" , notes="服务员再次下单" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "againOrder", method = RequestMethod.POST)
    public Result againOrder(@RequestBody AddOrderReq body) {
        return orderService.againOrder(body);
    }
}
