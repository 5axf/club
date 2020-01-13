package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.food.Dish;
import com.sky.car.business.entity.food.DishReq;
import com.sky.car.business.entity.food.Menu;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.order.*;
import com.sky.car.business.entity.user.UserRes;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.food.DishService;
import com.sky.car.business.service.food.MenuService;
import com.sky.car.business.service.hours.HoursService;
import com.sky.car.business.service.order.MenuOneRes;
import com.sky.car.business.service.order.MenuTwoRes;
import com.sky.car.business.service.order.OrderDishService;
import com.sky.car.business.service.order.OrderService;
import com.sky.car.business.service.room.RoomService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/wx/order/")
@Api(tags = { "订单管理接口" })
public class WxOrderController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DishService dishService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private HoursService hoursService;
    @Autowired
    private OrderDishService orderDishService;

    @ApiOperation(value="查询所有分类菜品" , notes="查询所有分类菜品" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findAllDishByMenu", method = RequestMethod.POST)
    public Result findAllDishByMenu(@RequestBody DishReq body){
        Wrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper.eq("isFrame",1);
        wrapper.eq("isDel" ,1);
        wrapper.eq("level",1);
        List<Menu> fMenus = menuService.selectList(wrapper);
        if (Utils.isEmpty(fMenus) || fMenus.size() == 0){
            return Result.failResult("菜品为空");
        }
        List<MenuOneRes> menuOneResList = new ArrayList<>();
        for (Menu menu : fMenus) {
            MenuOneRes menuOneRes = new MenuOneRes();
            BeanUtils.copyProperties(menu,menuOneRes);
            Wrapper<Menu> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("isFrame",1);
            wrapper1.eq("isDel" ,1);
            wrapper1.eq("parentMenuId",menu.getMenuId());
            wrapper1.eq("level",2);
            List<Menu> tMenus = menuService.selectList(wrapper1);
            if (Utils.isEmpty(tMenus) || tMenus.size() == 0){
                continue;
            }
            List<MenuTwoRes> menuTwoResList = new ArrayList<>();
            for (Menu tMenu : tMenus) {
                MenuTwoRes  menuTwoRes = new MenuTwoRes();
                BeanUtils.copyProperties(tMenu,menuTwoRes);
                menuTwoResList.add(menuTwoRes);
                Wrapper<Dish> wrapper2 = new EntityWrapper<>();
                wrapper2.eq("menuId",tMenu.getMenuId());
                wrapper2.eq("isFrame",1);
                wrapper2.eq("isDel",1);
                List<Dish> dishes = dishService.selectList(wrapper2);
                if (Utils.isNotEmpty(dishes)){
                    menuTwoRes.setDishList(dishes);
                }
            }
            menuOneRes.setMenuTwoRes(menuTwoResList);
            menuOneResList.add(menuOneRes);
        }
        return Result.successResult(menuOneResList);
    }


    @ApiOperation(value="用户下单" , notes="用户下单" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    public Result addOrder(@RequestBody AddOrderReq body){
       return orderService.addOrder(body);
    }

    @ApiOperation(value="查询所有订单" , notes="根据用户查询所有订单" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findOrderByUser", method = RequestMethod.POST)
    public Result findOrderByUser(@RequestBody AddOrderReq body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        UserRes userRes = (UserRes) userToken.getSessionMap().get(userToken.getSessionKey());
        Wrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.ne("state",7);
        wrapper.eq("userId",userRes.getUserid());
        wrapper.orderBy("creaTime",false);
        List<Order> orders = orderService.selectList(wrapper);
        if (Utils.isEmpty(orders) || orders.size() == 0){
            return Result.successResult("暂无订单");
        }
        List<FindOrderRes> list = new ArrayList<>();
        for (Order order : orders) {
            FindOrderRes orderRes = new FindOrderRes();
            Hours hours = hoursService.selectById(order.getHoursId());
            BeanUtils.copyProperties(order, orderRes);
            orderRes.setHoursName(hours.getHoursName());
            list.add(orderRes);
        }
        return Result.successResult(list);
    }

    @ApiOperation(value="用戶取消预订单操作" , notes="用戶取消预订单操作" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "cancelOrder", method = RequestMethod.POST)
    public Result cancelOrder(@RequestBody OrderReq body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        UserRes userRes = (UserRes) userToken.getSessionMap().get(userToken.getSessionKey());
        if (Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if (Utils.isEmpty(order)){
            return Result.failResult("订单错误");
        }
        if (order.getState() == 2){
            return Result.failResult("预订单已取消");
        }else if (order.getState() != 1){
            return Result.failResult("该订单不可取消");
        }
        order.setState(2);
        order.setUpdateTime(new Date());
        boolean b = orderService.updateById(order);
        if (b){
            return Result.successResult("操作成功");
        }
        return Result.failResult("操作失败");
    }

    @ApiOperation(value="查询订单详情" , notes="查询单个订单详情" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findOneOredrRes", method = RequestMethod.POST)
    public Result findOneOredrRes(@RequestBody OrderReq body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        UserRes userRes = (UserRes) userToken.getSessionMap().get(userToken.getSessionKey());
        if (Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数为空");
        }
        Order order = orderService.selectById(body.getOrderId());
        if (Utils.isEmpty(order)){
            return Result.failResult("订单错误");
        }
        Wrapper<OrderDish> wrapper = new EntityWrapper<>();
        wrapper.eq("orderId",order.getOrderId());
        wrapper.eq("state",1);
        List<OrderDish> orderDishes = orderDishService.selectList(wrapper);
        Hours hours = hoursService.selectById(order.getHoursId());
        Map map = new HashMap<>();
        map.put("order",order);
        map.put("dish",orderDishes);
        map.put("hours",hours);
        return Result.successResult(map);
    }

    @ApiOperation(value="用戶结算订单" , notes="用戶结算订单" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "accountOrder", method = RequestMethod.POST)
    public Result accountOrder(@RequestBody OrderReq body){
        return orderService.accountOrder(body);
    }

}
