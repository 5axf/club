package com.sky.car.util;

import java.io.Serializable;

import com.baomidou.mybatisplus.plugins.Page;


public class PageUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    public static int defeatCurrent = 1;
    public static int defeatSize = 15;

    private Integer pageCurrent;
    private Integer pageSize;

    public PageUtils() {

    }

    public static PageUtils newInstant() {
        return new PageUtils();
    }

    public PageUtils(Integer pageCurrent, Integer pageSize) {
        this.pageCurrent = pageCurrent;
        this.pageSize = pageSize;
    }

    public Integer getPageCurrent() {
        return (pageCurrent != null && pageCurrent != 0) ? pageCurrent : defeatCurrent;
    }

    public void setPageCurrent(Integer pageCurrent) {
        this.pageCurrent = pageCurrent;
    }

    public Integer getPageSize() {
        return (pageSize != null && pageCurrent != 0) ? pageSize : defeatSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public <T> Page<T> getPage() {
        return new Page<T>(getPageCurrent(), getPageSize());
    }

    public <T> Page<T> getPage(int current, int size) {
        return new PageUtils(current, size).getPage();
    }

}
