package com.sky.car.business.entity.hours;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author axf
 * @since 2020-01-07
 */
public class HoursReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name="hoursId" , value="时间段id")
    private Integer hoursId;
    @ApiModelProperty(name="roomName" , value="时间段名称")
    private String hoursName;


    @ApiModelProperty(name="hourseStartTime" , value="开始时间")
    private String hourseStartTime;


    @ApiModelProperty(name="hourseEndTime" , value="结束时间")
    private String hourseEndTime;

    @ApiModelProperty(name="isDel" , value="是否显示，1:显示；2：不显示")
    private Integer isView;

    @ApiModelProperty(name="token" , value="token令牌")
    private String token;

    @ApiModelProperty(name="current" , value="当前页码")
    private int current;

    @ApiModelProperty(name="size" , value="每页记录")
    private int size;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getHoursId() {
        return hoursId;
    }

    public void setHoursId(Integer hoursId) {
        this.hoursId = hoursId;
    }

    public String getHoursName() {
        return hoursName;
    }

    public void setHoursName(String hoursName) {
        this.hoursName = hoursName;
    }

    public String getHourseStartTime() {
        return hourseStartTime;
    }

    public void setHourseStartTime(String hourseStartTime) {
        this.hourseStartTime = hourseStartTime;
    }

    public String getHourseEndTime() {
        return hourseEndTime;
    }

    public void setHourseEndTime(String hourseEndTime) {
        this.hourseEndTime = hourseEndTime;
    }

    public Integer getIsView() {
        return isView;
    }

    public void setIsView(Integer isView) {
        this.isView = isView;
    }
}
