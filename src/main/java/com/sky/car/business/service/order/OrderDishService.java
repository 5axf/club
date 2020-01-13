package com.sky.car.business.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.food.Dish;
import com.sky.car.business.entity.order.Order;
import com.sky.car.business.entity.order.OrderDish;
import com.sky.car.business.entity.order.OrderDishReq;
import com.sky.car.business.mapper.order.OrderDishMapper;
import com.sky.car.business.service.food.DishService;
import com.sky.car.common.Result;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import com.sky.car.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDishService extends BaseService<OrderDishMapper,OrderDish> {

    @Autowired
    private OrderDishMapper orderDishMapper;

    @Override
    public BaseMapper<OrderDish> getDao() {
        return orderDishMapper;
    }


    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDishService orderDishService;
    @Autowired
    private DishService dishService;

    /**
     *
     * @param body
     * @return
     */
    @Transactional
    public Result addOrderDish(@RequestBody OrderDishReq body){
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        Dish dish1 = dishService.selectById(body.getDishId());
        if(Utils.isEmpty(dish1)){
            return Result.failResult("所选菜不存在");
        }
        if(2 == dish1.getIsFrame()){
            return Result.failResult("所选菜已下架");
        }
        Wrapper<OrderDish> wrapper = new EntityWrapper<>();
        wrapper.eq("orderId", order.getOrderId());
        List<OrderDish> orderDishes = orderDishService.selectList(wrapper);
        OrderDish dish = null;
        if(Utils.isNotEmpty(orderDishes)){
            for(OrderDish orderDish : orderDishes){
                if(orderDish.getDishId() == body.getDishId()){
                    dish = orderDish;
                }
            }
        }
        if(Utils.isNotEmpty(dish)){
            dish.setNum(body.getNum() + dish.getNum());
            boolean update = orderDishService.updateById(dish);
            if(update){
                //添加成功之后重新计算订单总金额
                boolean update2 = RecalculatedAmount(order);
                if(update2){
                    return Result.successResult("添加成功");
                }else {
                    return Result.failResult("添加失败");
                }
            }else {
                return Result.failResult("添加失败");
            }
        }else {
            OrderDish orderDish = new OrderDish();
            orderDish.setOrderId(body.getOrderId());
            orderDish.setOrderNum(body.getOrderNum());
            orderDish.setDishId(dish.getDishId());
            orderDish.setDishName(dish.getDishName());
            orderDish.setPrice(dish.getPrice());
            orderDish.setNum(body.getNum());
            orderDish.setState(1);
            orderDish.setCreatTime(new Date());
            boolean insert = orderDishService.insert(orderDish);
            if(insert){
                //添加成功之后重新计算订单总金额
                boolean update = RecalculatedAmount(order);
                if(update){
                    return Result.successResult("添加成功");
                }else {
                    return Result.failResult("添加失败");
                }
            }else {
                return Result.failResult("添加失败");
            }
        }
    }

    @Transactional
    public Result addOrderDishBatch(@RequestBody OrderDishReq body){
        Order order = orderService.selectById(body.getOrderId());
        if(Utils.isEmpty(order)){
            return Result.failResult("订单不存在");
        }
        //需要新增的菜品列表
        List<OrderDish> fromList = new ArrayList<>();
        if(Utils.isNotEmpty(body.getOrderDishList())){
            List<OrderDish> list = body.getOrderDishList();
            for(OrderDish dish : list){
                Dish dish1 = dishService.selectById(dish.getDishId());
                if(Utils.isEmpty(dish1)){
                    return Result.failResult("菜品id为"+dish.getDishId()+" 的菜品不存在，请检查");
                }
                if(dish1.getIsFrame() == 2){
                    return Result.failResult("所选菜"+dish1.getDishName()+"已下架，请重新选择");
                }
                OrderDish orderDish = new OrderDish();
                orderDish.setOrderId(body.getOrderId());
                orderDish.setOrderNum(body.getOrderNum());
                orderDish.setDishId(dish.getDishId());
                orderDish.setDishName(dish.getDishName());
                orderDish.setPrice(dish.getPrice());
                orderDish.setNum(body.getNum());
                orderDish.setState(1);
                orderDish.setCreatTime(new Date());
                fromList.add(orderDish);
            }

            Wrapper<OrderDish> wrapper = new EntityWrapper<>();
            wrapper.eq("orderId", order.getOrderId());
            //原有的菜品列表
            List<OrderDish> orderDishes = orderDishService.selectList(wrapper);

            //最终需要新增的菜品列表
            List<OrderDish> tempList = new ArrayList<>();
            boolean b = false;

            if(Utils.isNotEmpty(orderDishes) && Utils.isNotEmpty(fromList)){
                for (OrderDish od: orderDishes) {
                    for (OrderDish d: fromList) {
                        if(od.getDishId() == d.getDishId()){
                            od.setNum(od.getNum() + d.getNum());
                            b = orderDishService.updateById(od);
                        }else {
                            d.setCreatTime(new Date());
                            d.setState(1);
                            tempList.add(d);
                        }
                    }
                }
            }
            if(Utils.isNotEmpty(tempList)){
                b = orderDishService.insertBatch(tempList);
            }
            if(b){
                //添加成功之后重新计算订单总金额
                boolean update = RecalculatedAmount(order);
                if(update){
                    return Result.successResult("批量添加成功");
                }else {
                    return Result.failResult("批量添加失败");
                }
            }else {
                return Result.failResult("批量添加失败");
            }
        }
        return Result.failResult("批量添加失败");
    }


    /**
     * 重新计算订单总金额
     * @param order 订单
     * @return
     */
    public boolean RecalculatedAmount(Order order){
        Wrapper<OrderDish> wrapper = new EntityWrapper<>();
        wrapper.eq("orderId", order.getOrderId());
        List<OrderDish> orderDishes = orderDishService.selectList(wrapper);
        BigDecimal amount = new BigDecimal(0);
        for(OrderDish dish : orderDishes){
            amount = amount.add(dish.getPrice().multiply(new BigDecimal(dish.getNum())));
        }
        //设置保留两位小数
        DecimalFormat df1 = new DecimalFormat("#.00");
        String format = df1.format(amount);
        order.setOrderTotal(new BigDecimal(format));
        boolean update = orderService.updateById(order);
        return update;
    }

}
