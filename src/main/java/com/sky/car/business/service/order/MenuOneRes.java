package com.sky.car.business.service.order;

import java.util.List;

public class MenuOneRes {

    private Integer menuId;

    private String menuName;

    private String menuDesc;

    private String menuImg;

    private Integer level;

    private List<MenuTwoRes> menuTwoRes;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
