package com.sky.car.business.service.food;

import com.sky.car.business.entity.food.Menu;
import com.sky.car.business.mapper.food.MenuMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends BaseService<MenuMapper,Menu> {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public BaseMapper<Menu> getDao() {
        return menuMapper;
    }

}
