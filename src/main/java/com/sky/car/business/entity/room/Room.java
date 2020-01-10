package com.sky.car.business.entity.room;

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
@TableName("ty_room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增Id
     */
    @TableId(value = "roomId", type = IdType.AUTO)
    @ApiModelProperty(name="roomId" , value="房间id")
    private Integer roomId;
    @ApiModelProperty(name="roomName" , value="房间名称")
    private String roomName;
    @ApiModelProperty(name="roomDes" , value="房间描述")
    private String roomDes;
    @ApiModelProperty(name="roomImg" , value="房间图片")
    private String roomImg;
    @ApiModelProperty(name="isFrame" , value="是否上架，1：上架；2：下架")
    private Integer isFrame;
    @ApiModelProperty(name="isDel" , value="是否删除，1:未删除；2：已删除")
    private Integer isDel;
    @TableField(value="createTime" , fill= FieldFill.INSERT)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(value="updatetime" , fill=FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDes() {
        return roomDes;
    }

    public void setRoomDes(String roomDes) {
        this.roomDes = roomDes;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }

    public Integer getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(Integer isFrame) {
        this.isFrame = isFrame;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
