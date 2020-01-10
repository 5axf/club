package com.sky.car.business.entity.food;

import java.util.List;

public class MenuFirstRes {

    private Integer menuId;

    private String menuName;

    private String menuDesc;

    private String menuImg;

    private List<MenuTwoRes> menuTwoRes;

    private Integer isFrame;

    public Integer getIsFrame() {
        return isFrame;
    }

    public void setIsFrame(Integer isFrame) {
        this.isFrame = isFrame;
    }

    public List<MenuTwoRes> getMenuTwoRes() {
        return menuTwoRes;
    }

    public void setMenuTwoRes(List<MenuTwoRes> menuTwoRes) {
        this.menuTwoRes = menuTwoRes;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuImg() {
        return menuImg;
    }

    public void setMenuImg(String menuImg) {
        this.menuImg = menuImg;
    }
}
