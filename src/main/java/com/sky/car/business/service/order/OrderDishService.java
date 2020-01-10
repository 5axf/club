package com.sky.car.business.service.order;

import com.sky.car.business.entity.order.OrderDish;
import com.sky.car.business.mapper.order.OrderDishMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDishService extends BaseService<OrderDishMapper,OrderDish> {

    @Autowired
    private OrderDishMapper orderDishMapper;

    @Override
    public BaseMapper<OrderDish> getDao() {
        return orderDishMapper;
    }
}
