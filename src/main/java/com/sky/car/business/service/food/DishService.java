package com.sky.car.business.service.food;

import com.sky.car.business.entity.food.Dish;
import com.sky.car.business.mapper.food.DishMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService extends BaseService<DishMapper,Dish> {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public BaseMapper<Dish> getDao() {
        return dishMapper;
    }
}
