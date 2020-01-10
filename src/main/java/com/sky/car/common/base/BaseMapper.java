package com.sky.car.common.base;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.mapper.BaseMapper<T>{

    <P> T selectObject(P var1);

    <P> List<T> selectObjectList(P var1);

    <P> List<T> selectObjectList(Page<T> page, P var1);

    <K, V, P> Map<K, V> selectMap(P var1);

    <K, V, P> List<Map<K, V>> selectMapList(P var1);
    
    <K, V, P> List<Map<K, V>> selectMapList(Page<P> page, P var1);

}
