package com.sky.car.business.service.recharge;

import com.sky.car.business.entity.admin.Admin;
import com.sky.car.business.entity.recharge.Recharge;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.mapper.admin.AdminMapper;
import com.sky.car.business.mapper.recharge.RechargeMapper;
import com.sky.car.business.mapper.room.RoomMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RechargeService extends BaseService<RechargeMapper, Recharge>{

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public BaseMapper<Recharge> getDao() {
        return rechargeMapper;
    }

}
