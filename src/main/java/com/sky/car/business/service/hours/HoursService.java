package com.sky.car.business.service.hours;

import com.sky.car.business.entity.hours.Hours;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.mapper.hours.HoursMapper;
import com.sky.car.business.mapper.room.RoomMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoursService extends BaseService<HoursMapper,Hours>{
	
	@Autowired
    private HoursMapper hoursMapper;

	@Override
	public BaseMapper<Hours> getDao() {
		return hoursMapper;
	}

}
