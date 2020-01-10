package com.sky.car.business.service.room;

import com.sky.car.business.entity.admin.Admin;
import com.sky.car.business.entity.room.Room;
import com.sky.car.business.mapper.admin.AdminMapper;
import com.sky.car.business.mapper.room.RoomMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService extends BaseService<RoomMapper,Room>{
	
	@Autowired
    private RoomMapper roomMapper;

	@Override
	public BaseMapper<Room> getDao() {
		return roomMapper;
	}

}
