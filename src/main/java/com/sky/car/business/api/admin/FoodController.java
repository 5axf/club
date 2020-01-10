package com.sky.car.business.api.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sky.car.business.entity.food.*;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.food.DishService;
import com.sky.car.business.service.food.MenuService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.util.PageUtils;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

@RestController
@RequestMapping("/admin/food/")
@Api(tags = { "后台管理-菜品接口" })
public class FoodController {

    @Autowired
    MenuService menuService;
    @Autowired
    DishService dishService;
    @Autowired
    AdminTokenService adminTokenService;

    @ApiOperation(value="查询菜单" , notes="查询菜单,以List<一级<二级>>，形式返回" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findMenues", method = RequestMethod.POST)
    public Result findMenues(@RequestBody MenuReq body){
        // 查询第一集
        Wrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper.eq("level",1);
//        wrapper.eq("isFrame",1); // 上架
        wrapper.eq("isDel",1); // 未删除
        List<Menu> menusFirst = menuService.selectList(wrapper);
        if (Utils.isEmpty(menusFirst) || menusFirst.size() == 0){
            return Result.successResult("暂无数据");
        }
        List<MenuFirstRes> menuFirstResList = new ArrayList<>();
        for (Menu menu :menusFirst) {
            MenuFirstRes menuFirstRes = new MenuFirstRes();
            BeanUtils.copyProperties(menu,menuFirstRes);
            menuFirstResList.add(menuFirstRes);
        }
        for (MenuFirstRes menuFirstRes : menuFirstResList) {
            Wrapper<Menu> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("level",2);
//            wrapper1.eq("isFrame",1); // 上架
            wrapper1.eq("isDel",1); // 未删除
            wrapper1.eq("parentMenuId",menuFirstRes.getMenuId());
            List<Menu> menusTwo = menuService.selectList(wrapper1);
            List<MenuTwoRes> menuTwoResList = new ArrayList<>();
            if (Utils.isNotEmpty(menusTwo) && menusTwo.size() > 0){
                for (Menu menu :menusTwo) {
                    MenuTwoRes menuTwoRes = new MenuTwoRes();
                    BeanUtils.copyProperties(menu,menuTwoRes);
                    menuTwoResList.add(menuTwoRes);
                }
                menuFirstRes.setMenuTwoRes(menuTwoResList);
            }
        }
        return Result.successResult(menuFirstResList);
    }

    @ApiOperation(value="修改菜单" , notes="修改菜单" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "updateMenues", method = RequestMethod.POST)
    public Result updateMenues(@RequestBody MenuReq body){
        if (Utils.isEmpty(body.getMenuId())){
            return Result.failResult("参数为空");
        }
        Menu menu = menuService.selectById(body.getMenuId());
        if (Utils.isEmpty(menu)){
            return Result.failResult("未找到该分类");
        }

        if (Utils.isNotEmpty(body.getMenuName())){
            menu.setMenuName(body.getMenuName());
        }

        if (Utils.isNotEmpty(body.getMenuDesc())){
            menu.setMenuDesc(body.getMenuDesc());
        }

        if (Utils.isNotEmpty(body.getMenuImg())){
            menu.setMenuImg(body.getMenuImg());
        }

        if (Utils.isNotEmpty(body.getIsFrame())){
            menu.setIsFrame(body.getIsFrame());
        }
        menu.setUpdateTime(new Date());

        boolean b = menuService.updateById(menu);
        if (!b){
            return Result.failResult("操作失败");
        }
        Wrapper<Dish> wrapper = new EntityWrapper<>();
        wrapper.eq("menuId",menu.getMenuId());
        wrapper.eq("updateTime",new Date());
        List<Dish> dishes = dishService.selectList(wrapper);
        if (Utils.isNotEmpty(dishes) && dishes.size() > 0){
            for (Dish dish : dishes) {
                dish.setMenuName(menu.getMenuName());
                // 菜单下架，下面所有的菜品也下架
                if (menu.getIsFrame() == 2){
                    dish.setIsFrame(2);
                }
            }
            dishService.updateBatchById(dishes);
        }
        return Result.successResult("操作成功");
    }

    @ApiOperation(value="查询菜单" , notes="根据级别查询菜单" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findMenuLevel", method = RequestMethod.POST)
    public Result findMenuLevel(@RequestBody MenuReq body){
        if (Utils.isEmpty(body.getLevel())){
            return Result.failResult("参数为空");
        }
        Wrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper.eq("level",body.getLevel());
        wrapper.eq("isFrame",1); // 上架
        wrapper.eq("isDel",1); // 未删除
        List<Menu> menus = menuService.selectList(wrapper);
        if (Utils.isEmpty(menus) || menus.size() == 0){
            return Result.failResult("暂无数据");
        }
        return Result.successResult(menus);
    }

    @ApiOperation(value="查询菜单" , notes="根据一级菜单查询菜单" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findMenuTwoByFirst", method = RequestMethod.POST)
    public Result findMenuTwoByFirst(@RequestBody MenuReq body){
        if (Utils.isEmpty(body.getParentMenuId())){
            return Result.failResult("参数为空");
        }
        Menu menu = menuService.selectById(body.getParentMenuId());
        if (Utils.isEmpty(menu)){
            return Result.failResult("一级菜品分类未找到");
        }
        Wrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper.eq("level",2);
        wrapper.eq("parentMenuId",menu.getMenuId());
//        wrapper.eq("isFrame",1); // 上架
        wrapper.eq("isDel",1); // 未删除
        List<Menu> menus = menuService.selectList(wrapper);
        return Result.successResult(menus);
    }

    @ApiOperation(value="添加菜单" , notes="添加菜单" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "addMenues", method = RequestMethod.POST)
    public Result addMenues(@RequestBody MenuReq body){
        if (Utils.isEmpty(body.getMenuName())){
            return Result.failResult("菜品名称为空");
        }
        if (Utils.isEmpty(body.getMenuDesc())){
            return Result.failResult("菜品描述为空");
        }
//        if (Utils.isEmpty(body.getMenuImg())){
//            return Result.failResult("菜品图片为空");
//        }
        if (Utils.isEmpty(body.getLevel())){
            return Result.failResult("菜品级别为空");
        }

        Menu menu = new Menu();
        menu.setMenuName(body.getMenuName());
        menu.setMenuDesc(body.getMenuDesc());
//        menu.setMenuImg(body.getMenuImg());
        menu.setLevel(body.getLevel());
        if (body.getLevel() == 2){
           if (Utils.isEmpty(body.getParentMenuId())){
               return Result.failResult("请输入一级菜品");
           }
            menu.setParentMenuId(body.getParentMenuId());
        }
        menu.setIsFrame(body.getIsFrame());
        menu.setCreatTime(new Date());
        menu.setIsDel(1);
        boolean insert = menuService.insert(menu);
        if (!insert){
            return Result.failResult("菜品为空");
        }
        return Result.successResult("操作成功");
    }

    @ApiOperation(value="查找菜品" , notes="查找未删除菜品" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findDishes", method = RequestMethod.POST)
    public Result findDishes(@RequestBody DishReq body){
        Wrapper<Dish> wrapper = new EntityWrapper<>();
        wrapper.orderBy("dishId",false);
        wrapper.eq("isDel",1);
        if (Utils.isNotEmpty(body.getDishName())){
            wrapper.like("dishName",body.getDishName());
        }
        Page<Dish> page = PageUtils.newInstant().getPage(body.getCurrent(), body.getSize());
        page = dishService.selectPage(page, wrapper);
        return Result.successResult(page);
    }

    @ApiOperation(value="添加菜品" , notes="添加菜品" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "addDish", method = RequestMethod.POST)
    public Result addDish(@RequestBody DishReq body){
        if (Utils.isEmpty(body.getToken())){
            return Result.failResult("参数为空");
        }
        AdminToken adminToken = adminTokenService.checkToken(body.getToken());
//        if (Utils.isEmpty(adminToken)){
//            return Result.tokenInvalidResult();
//        }
        if (Utils.isEmpty(body.getDishName())){
            return Result.failResult("菜名为空");
        }
        if (Utils.isEmpty(body.getDishDesc())){
            return Result.failResult("菜名描述为空");
        }
        if (Utils.isEmpty(body.getFirstMenuId())){
            return Result.failResult("一级菜单为空");
        }
        if (Utils.isEmpty(body.getTwoMenuId())){
            return Result.failResult("二级菜单为空");
        }
        if (Utils.isEmpty(body.getDishImg())){
            return Result.failResult("菜品图片为空");
        }
        if (Utils.isEmpty(body.getPrice())){
            return Result.failResult("菜品价格为空");
        }

        Menu firstMenu = menuService.selectById(body.getFirstMenuId());
        if (Utils.isEmpty(firstMenu)){
            return Result.failResult("一级菜单未找到");
        }
        Wrapper<Menu> wrapper = new EntityWrapper<>();
        wrapper.eq("menuId",body.getTwoMenuId());
        wrapper.eq("level",2);
        wrapper.eq("isDel",1);
        Menu twoMenus = menuService.selectOne(wrapper);
        if (Utils.isEmpty(twoMenus)){
            return Result.failResult("二级菜单未找到");
        }

        Dish dish = new Dish();
        dish.setDishName(body.getDishName());
        dish.setDishDesc(body.getDishDesc());
        dish.setDishImg(body.getDishImg());
        DecimalFormat df =new DecimalFormat("#.00");
        String format = df.format(body.getPrice());
        dish.setPrice(new BigDecimal(format));
        dish.setMenuId(twoMenus.getMenuId());
        dish.setMenuName(twoMenus.getMenuName());
        dish.setIsFrame(1);
        dish.setCreatTime(new Date());
        dish.setIsDel(1);
        boolean b = dishService.insert(dish);
        if (!b){
            return Result.failResult("操作失败");
        }
        return Result.successResult();
    }

    @ApiOperation(value="修改菜品" , notes="修改菜品" , response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "updateDish", method = RequestMethod.POST)
    public Result updateDish(@RequestBody DishReq body){
        if (Utils.isEmpty(body.getDishId())){
            return Result.failResult("参数为空");
        }
        Dish dish = dishService.selectById(body.getDishId());
        if (Utils.isEmpty(dish)){
            return Result.failResult("菜品未找到");
        }
        if (Utils.isNotEmpty(body.getIsFrame())){
            dish.setIsFrame(body.getIsFrame());
        }
        if(Utils.isNotEmpty(body.getDishName())){
            dish.setDishName(body.getDishName());
        }
        if (Utils.isNotEmpty(body.getDishDesc())){
            dish.setDishDesc(body.getDishDesc());
        }
        if (Utils.isNotEmpty(body.getDishImg())){
            dish.setDishImg(body.getDishImg());
        }
        if (Utils.isNotEmpty(body.getPrice())){
            DecimalFormat df =new DecimalFormat("#.00");
            String format = df.format(body.getPrice());
            dish.setPrice(new BigDecimal(format));
        }
        dish.setUpdateTime(new Date());
        boolean b = dishService.updateById(dish);
        if (!b){
            return Result.failResult("操作失败");
        }
        return Result.successResult("操作成功");
    }

}
