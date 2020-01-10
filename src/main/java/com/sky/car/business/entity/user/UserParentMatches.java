package com.sky.car.business.entity.user;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 余晓翔
 * @since 2019-04-22
 */
@TableName("ty_user_parent_matches")
public class UserParentMatches implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "userParentMatchesId", type = IdType.AUTO)
    private Integer userParentMatchesId;

    @TableField("userParentMatchesName")
    private String userParentMatchesName;

    @TableField("parentMatchesId")
    private Integer parentMatchesId;

    /**
     * 母赛事名称
     */
    @TableField("parentMatchesName")
    private String parentMatchesName;

    @TableField("userId")
    private Integer userId;

    @TableField("userName")
    private String userName;

    private String logo;

    /**
     * 0未发布、1发布（报名中）
     */
    private Integer status;

    /**
     * 母赛事子状态
     */
    @TableField("subStatus")
    private Integer subStatus;

    private String type;

    /**
     * 母赛事类型（团体赛team、个人赛personal)
     */
    @TableField("matchesType")
    private String matchesType;

    @TableField("totalPlayer")
    private Integer totalPlayer;

    @TableField("totalTeam")
    private Integer totalTeam;

    @TableField("sceneId")
    private Integer sceneId;

    @TableField("sceneName")
    private String sceneName;

    @TableField("aliasTime")
    private String aliasTime;

    @TableField("startTime")
    private Date startTime;

    @TableField("endTime")
    private Date endTime;

    /**
     * 小程序简介图片
     */
    @TableField("miniProfileImage")
    private String miniProfileImage;

    private Integer top;

    private String remark;

    private Integer recommend;

    /**
     * 逻辑删除，添加赛事基本信息默认YES
     */
    @TableField("isDelete")
    private String isDelete;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    /**
     * 赛事地址
     */
    @TableField("matchesAddress")
    private String matchesAddress;

    /**
     * 团体赛子类型
     */
    @TableField("matchesSubType")
    private String matchesSubType;

    @TableField("matchesArea")
    private String matchesArea;

    @TableField("flowPic")
    private String flowPic;

    @TableField("rulePic")
    private String rulePic;

    @TableField("awardPic")
    private String awardPic;

    @TableField("notesPic")
    private String notesPic;

    /**
     * 状态1
     */
    private Integer status1;

    /**
     * 状态2
     */
    private Integer status2;

    /**
     * 状态3
     */
    private Integer status3;

    /**
     * 状态4
     */
    private Integer status4;


    public Integer getUserParentMatchesId() {
        return userParentMatchesId;
    }

    public void setUserParentMatchesId(Integer userParentMatchesId) {
        this.userParentMatchesId = userParentMatchesId;
    }

    public String getUserParentMatchesName() {
        return userParentMatchesName;
    }

    public void setUserParentMatchesName(String userParentMatchesName) {
        this.userParentMatchesName = userParentMatchesName;
    }

    public Integer getParentMatchesId() {
        return parentMatchesId;
    }

    public void setParentMatchesId(Integer parentMatchesId) {
        this.parentMatchesId = parentMatchesId;
    }

    public String getParentMatchesName() {
        return parentMatchesName;
    }

    public void setParentMatchesName(String parentMatchesName) {
        this.parentMatchesName = parentMatchesName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(Integer subStatus) {
        this.subStatus = subStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMatchesType() {
        return matchesType;
    }

    public void setMatchesType(String matchesType) {
        this.matchesType = matchesType;
    }

    public Integer getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(Integer totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

    public Integer getTotalTeam() {
        return totalTeam;
    }

    public void setTotalTeam(Integer totalTeam) {
        this.totalTeam = totalTeam;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getAliasTime() {
        return aliasTime;
    }

    public void setAliasTime(String aliasTime) {
        this.aliasTime = aliasTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMiniProfileImage() {
        return miniProfileImage;
    }

    public void setMiniProfileImage(String miniProfileImage) {
        this.miniProfileImage = miniProfileImage;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
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

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getMatchesAddress() {
        return matchesAddress;
    }

    public void setMatchesAddress(String matchesAddress) {
        this.matchesAddress = matchesAddress;
    }

    public String getMatchesSubType() {
        return matchesSubType;
    }

    public void setMatchesSubType(String matchesSubType) {
        this.matchesSubType = matchesSubType;
    }

    public String getMatchesArea() {
        return matchesArea;
    }

    public void setMatchesArea(String matchesArea) {
        this.matchesArea = matchesArea;
    }

    public String getFlowPic() {
        return flowPic;
    }

    public void setFlowPic(String flowPic) {
        this.flowPic = flowPic;
    }

    public String getRulePic() {
        return rulePic;
    }

    public void setRulePic(String rulePic) {
        this.rulePic = rulePic;
    }

    public String getAwardPic() {
        return awardPic;
    }

    public void setAwardPic(String awardPic) {
        this.awardPic = awardPic;
    }

    public String getNotesPic() {
        return notesPic;
    }

    public void setNotesPic(String notesPic) {
        this.notesPic = notesPic;
    }

    public Integer getStatus1() {
        return status1;
    }

    public void setStatus1(Integer status1) {
        this.status1 = status1;
    }

    public Integer getStatus2() {
        return status2;
    }

    public void setStatus2(Integer status2) {
        this.status2 = status2;
    }

    public Integer getStatus3() {
        return status3;
    }

    public void setStatus3(Integer status3) {
        this.status3 = status3;
    }

    public Integer getStatus4() {
        return status4;
    }

    public void setStatus4(Integer status4) {
        this.status4 = status4;
    }

    @Override
    public String toString() {
        return "UserParentMatches{" +
        "userParentMatchesId=" + userParentMatchesId +
        ", userParentMatchesName=" + userParentMatchesName +
        ", parentMatchesId=" + parentMatchesId +
        ", parentMatchesName=" + parentMatchesName +
        ", userId=" + userId +
        ", userName=" + userName +
        ", logo=" + logo +
        ", status=" + status +
        ", subStatus=" + subStatus +
        ", type=" + type +
        ", matchesType=" + matchesType +
        ", totalPlayer=" + totalPlayer +
        ", totalTeam=" + totalTeam +
        ", sceneId=" + sceneId +
        ", sceneName=" + sceneName +
        ", aliasTime=" + aliasTime +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", miniProfileImage=" + miniProfileImage +
        ", top=" + top +
        ", remark=" + remark +
        ", recommend=" + recommend +
        ", isDelete=" + isDelete +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", attribute1=" + attribute1 +
        ", attribute2=" + attribute2 +
        ", attribute3=" + attribute3 +
        ", attribute4=" + attribute4 +
        ", matchesAddress=" + matchesAddress +
        ", matchesSubType=" + matchesSubType +
        ", matchesArea=" + matchesArea +
        ", flowPic=" + flowPic +
        ", rulePic=" + rulePic +
        ", awardPic=" + awardPic +
        ", notesPic=" + notesPic +
        ", status1=" + status1 +
        ", status2=" + status2 +
        ", status3=" + status3 +
        ", status4=" + status4 +
        "}";
    }
}
