package com.sky.car.common.base;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T>{
	
	public abstract BaseMapper<T> getDao() ;

	/**
	 * 多表查询分页
	 */
	public Page<Map<String, Object>> selectMapsPage(Page<Map<String, Object>> page, Map<String, Object> condition) {
		return page.setRecords(this.getDao().selectMapList(page,condition));
	}
	
	/**
	 * 多表查询分页
	 */
	public Page<T> selectListPage(Page<T> page, Map<String, Object> condition) {
		return page.setRecords(this.getDao().selectObjectList(page,condition));
	}
	
	/**
	 * 多表查询
	 */
	public List<Map<String, Object>> selectMaps(Map<String, Object> condition) {
		return this.getDao().selectMapList(condition);
	}
	
	/**
	 * 多表查询
	 */
	public List<T> selectList(Map<String, Object> condition) {
		return this.getDao().selectObjectList(condition);
	}

}
