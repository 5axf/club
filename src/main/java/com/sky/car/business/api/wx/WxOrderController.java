package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.food.Dish;
import com.sky.car.business.entity.food.DishReq;
import com.sky.car.business.entity.food.Menu;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.order.AddOrderReq;
import com.sky.car.business.entity.order.Order;
import com.sky.car.business.entity.order.OrderDish;
import com.sky.car.business.entity.room.Room;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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




}
