package com.sky.car.config;

public class WxPayConfig {
    //小程序appid
    public static final String appid = "wxdb284d4f007af002";
    //微信支付的商户id
    public static final String mch_id = "1507837931";
    //微信支付的商户密钥
    public static final String key = "Ty2020TiaNYuSports2clUb2golf2nET";
    //支付成功后的服务器回调url
    public static final String notify_url = "https://club.tianyusports.com/wx/pay/wxNotify";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信订单查询单接口地址
    public static final String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";
}
