package com.sky.car.business.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.food.Dish;
import com.sky.car.business.entity.food.DishReq;
import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.order.AddOrderReq;
import com.sky.car.business.entity.order.Order;
import com.sky.car.business.entity.order.OrderDish;
import com.sky.car.business.entity.order.OrderReq;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.entity.user.UserRes;
import com.sky.car.business.mapper.order.OrderMapper;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.food.DishService;
import com.sky.car.business.service.food.MenuService;
import com.sky.car.business.service.hours.HoursService;
import com.sky.car.business.service.room.RoomService;
import com.sky.car.business.service.user.UserService;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService extends BaseService<OrderMapper, Order>{

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;
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
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = df.format(new Date());
        Order order = new Order();
        order.setOrderNum("BK"+ s);
        order.setUserId((userRes.getUserid()));
        order.setPhone(userRes.getMobile());
        order.setRoomId(room.getRoomId());
        order.setRoomName(room.getRoomName());
        order.setUseNum(body.getUseNum());
        order.setHoursId(hours.getHoursId());
        order.setHourTime(hours.getHourseStartTime() + "-" + hours.getHourseEndTime());
        order.setOrderDate(body.getOrderDate());
        order.setState(7); // 费单
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
                orderDish.setPrice(dish.getPrice());
                orderDishList.add(orderDish);
                BigDecimal dishPrice = new BigDecimal(dish.getPrice() + "");
                BigDecimal num = new BigDecimal(dishReq.getNum() + "");
                BigDecimal price = dishPrice.multiply(num);
                totalPrice = totalPrice.add(price);
            }
            // 前端的金额
            BigDecimal temp = new BigDecimal(body.getOrderTotal());
            DecimalFormat df2 = new DecimalFormat("0.00");
            String str = df2.format(temp);
            BigDecimal beforeTotal = new BigDecimal(str);

            // 后台计算
            BigDecimal temp1 = new BigDecimal(totalPrice+ "");
            DecimalFormat df3 = new DecimalFormat("0.00");
            String str1 = df3.format(temp);
            BigDecimal afterTotal = new BigDecimal(str1);

            // 价格一致时
            if (afterTotal.compareTo(beforeTotal) == 0){
                orderDishService.insertBatch(orderDishList);
                order.setOrderTotal(totalPrice);
                order.setState(1);
                this.updateById(order);
            }
        }
        return Result.successResult("下单成功");
    }

    @Transactional
    public Result accountOrder(@RequestBody OrderReq body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("userId",userToken.getUserId());
        User user = userService.selectOne(wrapper);
        if (Utils.isEmpty(user)){
            return Result.failResult("操作失败");
        }
        if (user.getStatus() != 4){
            return Result.failResult("请先成为会员");
        }

        if (Utils.isEmpty(body.getOrderId())){
            return Result.failResult("参数为空");
        }
        Order order = this.selectById(body.getOrderId());
        if (Utils.isEmpty(order)){
            return Result.failResult("订单错误");
        }
        if (order.getState() == 6){
            return Result.failResult("该订单已完成");
        }
        if (order.getState() != 4){
            return Result.failResult("操作失败");
        }
        if (order.getIsDish() == 2){
            return Result.failResult("未点餐，无法结算");
        }

        // 查询用户下的余额
        BigDecimal orderTotal = new BigDecimal(order.getOrderTotal() + "");
        BigDecimal balance = new BigDecimal(user.getBalance() + "");
        if (orderTotal.compareTo(balance) > 0){
            return Result.failResult("余额不足，请先充值");

        }
        // 用户支付后的余额
        BigDecimal subtract = balance.subtract(orderTotal);

        order.setState(6);// 设置订单已完成
        order.setPayWey(1);
        order.setCurBalance(subtract);
        order.setUpdateTime(new Date());
        order.setOrderDate(new Date());
        boolean b = this.updateById(order);
        if (b){
            user.setBalance(subtract);
            user.setUpdateTime(new Date());
            boolean b1 = userService.updateById(user);
            if (b1){
                return Result.successResult("结算成功");
            }
        }
        return Result.failResult("操作失败");
    }

    @Transactional
    public Result againOrder(@RequestBody AddOrderReq body) {
        if (Utils.isEmpty(body.getOldOrderId())) {
            return Result.failResult("参数为空");
        }
        Order oldOrder = this.selectById(body.getOldOrderId());
        if (Utils.isEmpty(oldOrder)) {
            return Result.failResult("参数错误");
        }
        if (oldOrder.getState() != 6) {
            return Result.failResult("暂不支持");
        }
        if (Utils.isEmpty(body.getList()) || body.getList().size() == 0) {
            return Result.failResult("请选择菜品");
        }
        // 用于生成订单编号
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = df.format(new Date());
        Order order = new Order();
        order.setOrderNum("BK" + s);
        order.setUserId((oldOrder.getUserId()));
        order.setPhone(oldOrder.getPhone());
        order.setRoomId(oldOrder.getRoomId());
        order.setRoomName(oldOrder.getRoomName());
        order.setUseNum(oldOrder.getUseNum());
        order.setHoursId(oldOrder.getHoursId());
        order.setHourTime(oldOrder.getHourTime());
        order.setOrderDate(oldOrder.getOrderDate());
        order.setState(4);
        order.setRemark("这是增加订单，原订单是" + oldOrder.getOrderId());
        order.setChildNum(oldOrder.getChildNum());
        order.setCreatTime(new Date());
        order.setIsDish(7);
        this.insert(order);
        if (Utils.isNotEmpty(body.getList()) && body.getList().size() > 0) {
            // 插入订单菜品
            List<OrderDish> orderDishList = new ArrayList<>();
            BigDecimal totalPrice = new BigDecimal("0.00");
            for (DishReq dishReq : body.getList()) {
                Dish dish = dishService.selectById(dishReq.getDishId());
                if (Utils.isEmpty(dish) || dish.getIsDel() == 2 || dish.getIsFrame() == 0) {
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
                orderDish.setPrice(dish.getPrice());
                orderDishList.add(orderDish);
                BigDecimal dishPrice = new BigDecimal(dish.getPrice() + "");
                BigDecimal num = new BigDecimal(dishReq.getNum() + "");
                BigDecimal price = dishPrice.multiply(num);
                totalPrice = totalPrice.add(price);
            }
            // 前端的金额
            BigDecimal temp = new BigDecimal(body.getOrderTotal());
            DecimalFormat df2 = new DecimalFormat("0.00");
            String str = df2.format(temp);
            BigDecimal beforeTotal = new BigDecimal(str);

            // 后台计算
            BigDecimal temp1 = new BigDecimal(totalPrice+ "");
            DecimalFormat df3 = new DecimalFormat("0.00");
            String str1 = df3.format(temp);
            BigDecimal afterTotal = new BigDecimal(str1);
            // 价格一致时
            if (afterTotal.compareTo(beforeTotal) == 0){
                orderDishService.insertBatch(orderDishList);
                order.setOrderTotal(totalPrice);
                order.setState(4);
                this.updateById(order);
            }
        }
        return Result.successResult("下单成功");
    }

}
