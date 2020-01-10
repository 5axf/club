package com.sky.car.business.service.order;

import com.sky.car.business.entity.food.Dish;
import com.sky.car.business.entity.food.DishReq;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.order.AddOrderReq;
import com.sky.car.business.entity.order.Order;
import com.sky.car.business.entity.order.OrderDish;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.entity.user.UserRes;
import com.sky.car.business.mapper.order.OrderMapper;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.food.DishService;
import com.sky.car.business.service.food.MenuService;
import com.sky.car.business.service.hours.HoursService;
import com.sky.car.business.service.room.RoomService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import com.sky.car.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService extends BaseService<OrderMapper, Order>{

    @Autowired
    private OrderMapper orderMapper;

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
    @Autowired
    private UserTokenService userTokenService;

    @Override
    public BaseMapper<Order> getDao() {
        return orderMapper;
    }

    @Transactional
    public Result addOrder(@RequestBody AddOrderReq body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        UserRes userRes = (UserRes) userToken.getSessionMap().get(userToken.getSessionKey());

        if (Utils.isEmpty(body.getRoomId())){
            return Result.failResult("请选择房间");
        }
        if (Utils.isEmpty(body.getOrderDate())){
            return Result.failResult("请选择日期");
        }
        if (Utils.isEmpty(body.getHoursId())){
            return Result.failResult("请选择时间");
        }
        if (Utils.isNotEmpty(body.getList()) && body.getOrderTotal()  == 0.00 ){
            return Result.failResult("操作失败");
        }
        if (Utils.isEmpty(body.getUseNum())){
            return Result.failResult("请输入用餐人数");
        }

        if (Utils.isEmpty(body.getIsDish())){
            return Result.failResult("操作失败");
        }
        Room room = roomService.selectById(body.getRoomId());
        if (Utils.isEmpty(room) || room.getIsFrame() == 2 || room.getIsDel() == 2){
            return Result.failResult("操作失败");
        }

        Hours hours = hoursService.selectById(body.getHoursId());
        if (Utils.isEmpty(hours) || hours.getIsView() == 2){
            return Result.failResult("操作失败");
        }
        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");

        // 用于生成订单编号
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = df.format(new Date());
        Order order = new Order();
        order.setOrderNum("BK"+ s);
        order.setUserId((userRes.getUserid()));
        order.setPhone(userRes.getMobile());
        order.setRoomId(room.getRoomId());
        order.setUseNum(body.getUseNum());
        order.setHoursId(hours.getHoursId());
        order.setHourTime(hours.getHourseStartTime() + "-" + hours.getHourseEndTime());
        order.setOrderDate(body.getOrderDate());
        order.setState(1); // 待审核
        if (Utils.isNotEmpty(body.getRemark())){
            order.setRemark(body.getRemark());
        }
        if (Utils.isNotEmpty(body.getChildNum())){
            order.setChildNum(body.getChildNum());
        }
        order.setCreatTime(new Date());
        order.setIsDish(body.getIsDish());
        this.insert(order);
        if (Utils.isNotEmpty(body.getList()) && body.getList().size() > 0){
            // 插入订单菜品
            List<OrderDish> orderDishList = new ArrayList<>();
            BigDecimal totalPrice = new BigDecimal("0.00");
            for (DishReq dishReq : body.getList()) {
                Dish dish = dishService.selectById(dishReq.getDishId());
                if (Utils.isEmpty(dish) || dish.getIsDel() == 2 || dish.getIsFrame() == 0){
                    return Result.failResult(dishReq.getDishName() + "菜品已下架，请重新选择");
                }
                OrderDish orderDish = new OrderDish();
                orderDish.setOrderId(order.getOrderId());
                orderDish.setOrderNum(order.getOrderNum());
                orderDish.setDishId(dish.getDishId());
                orderDish.setDishName(dish.getDishName());
                orderDish.setNum(dishReq.getNum());
                orderDish.setState(1);
                orderDish.setCreatTime(new Date());
                orderDishList.add(orderDish);
                BigDecimal dishPrice = new BigDecimal(dish.getPrice() + "");
                BigDecimal num = new BigDecimal(dishReq.getNum() + "");
                BigDecimal price = dishPrice.multiply(num);
                totalPrice.add(price);
            }
            BigDecimal orderTotal = new BigDecimal(body.getOrderTotal());
            // 价格一致时
            if (totalPrice.compareTo(orderTotal) == 0){
                orderDishService.insertBatch(orderDishList);
                order.setOrderTotal(totalPrice);
            }else{
                order.setState(7);
            }
            this.updateById(order);
        }
        return Result.successResult("下单成功");
    }

}
