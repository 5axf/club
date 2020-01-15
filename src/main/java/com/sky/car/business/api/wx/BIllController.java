package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.bill.BillReq;
import com.sky.car.business.entity.bill.BillRes;
import com.sky.car.business.entity.order.Order;
import com.sky.car.business.entity.recharge.Recharge;
import com.sky.car.business.entity.user.UserRes;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.order.OrderService;
import com.sky.car.business.service.recharge.RechargeService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/wx/bill/")
@Api(tags = { "订单管理接口" })
public class BIllController {

    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RechargeService rechargeService;

    @ApiOperation(value="我的账单查询" , notes="我的账单查询" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "findAllBill", method = RequestMethod.POST)
    public Result findAllBill(@RequestBody BillReq body){
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        UserRes userRes = (UserRes) userToken.getSessionMap().get(userToken.getSessionKey());

        List<BillRes> list = new ArrayList<>();

        // 查询Order
        Wrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.eq("state",6);
        wrapper.eq("userId",userRes.getUserid());
        List<Order> orders = orderService.selectList(wrapper);
        if (Utils.isNotEmpty(orders) && orders.size() > 0){
            for (Order order : orders) {
                BillRes billRes = new BillRes();
                billRes.setType(1);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                billRes.setTime(order.getOrderDate());
                billRes.setBalance(order.getCurBalance());
                billRes.setPrice(order.getOrderTotal());
                list.add(billRes);
            }
        }
        // 查询充值
        Wrapper<Recharge> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("userId",userRes.getUserid());
        wrapper1.eq("status",1);
        List<Recharge> recharges = rechargeService.selectList(wrapper1);
        if (Utils.isNotEmpty(recharges) && recharges.size() > 0){
            for (Recharge recharge : recharges) {
                BillRes billRes = new BillRes();
                billRes.setType(2);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                billRes.setTime(recharge.getCreateTime());
                billRes.setBalance(recharge.getBalance());
                billRes.setPrice(recharge.getAmount());
                list.add(billRes);
            }
        }
        if (list.size() == 0){
            return Result.successResult("暂无数据");
        }
        Collections.sort(list);
        return Result.successResult(list);

    }



}
