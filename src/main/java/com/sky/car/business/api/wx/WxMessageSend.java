package com.sky.car.business.api.wx;

import com.sky.car.business.entity.wxMessage.TemplateData;
import com.sky.car.business.entity.wxMessage.WxMssVo;
import com.sky.car.common.Result;
import com.sky.car.util.WXUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序消息
 */
@RestController
@RequestMapping("/wx/sendMessage/")
public class WxMessageSend {

    @Value("${appid}")
    private String appid;
    @Value("${secret}")
    private String secret;

    @ApiOperation(value="发送订阅消息" , notes="发送订阅消息", response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "openid",value = "登录标识",required = false),
    })
    @RequestMapping(value = "pushOneUser", method = RequestMethod.POST)
    public String pushOneUser(String openid) {
        return pushWxMessage("og0g-5TjLK7Ggh-8CcWYcVXOsuhI");
    }

    public String pushWxMessage(String openid) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = new Date();
        String str = "";
        str = sdf.format(temp);

        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(openid);//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id("5-8k__HCdDYX5JTQepyw-GK2wG_-tFuxz_8XIv1sUIw");//订阅消息模板id
        wxMssVo.setPage("pages/index/index");
        //参数填充
        Map<String, TemplateData> m = new HashMap<>();
        m.put("thing1", new TemplateData("账户充值"));
        m.put("character_string2", new TemplateData("VIP0001"));
        m.put("time3", new TemplateData(str));
        m.put("amount4", new TemplateData("100000元"));
        m.put("character_string5", new TemplateData("CA000000001"));
        wxMssVo.setData(m);

        //消息推送
        String responseRuslt = WXUtil.postForEntity(WXUtil.getAccessToken(appid,secret),wxMssVo);
        System.out.println("发送结果返回："+ responseRuslt);
        return responseRuslt;
    }

}
