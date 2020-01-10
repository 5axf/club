package com.sky.car.business.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.car.business.entity.admin.Admin;
import com.sky.car.business.mapper.admin.AdminMapper;
import com.sky.car.common.base.BaseMapper;
import com.sky.car.common.base.BaseService;

@Service
public class AdminService extends BaseService<AdminMapper,Admin>{
	
	@Autowired
    private AdminMapper adminMapper;

	@Override
	public BaseMapper<Admin> getDao() {
		return adminMapper;
	}

}
