package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.recharge.Recharge;
import com.sky.car.business.entity.recharge.RechargeReq;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.recharge.RechargeService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
import com.sky.car.util.DateUtil;
import com.sky.car.util.Utils;
import com.sky.car.util.WXUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序支付
 */
@RestController
@RequestMapping("/wx/pay/")
public class WxPayController {
    Logger log = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private UserService userService;
    @Autowired
    UserTokenService userTokenService;
    @Autowired
    private RechargeService rechargeService;

    @Value("${appid}")
    private String appid;//小程序ID
    @Value("${mchId}")
    private String mchId;//商户号
    private String tradeType = "JSAPI";//小程序交易类型
    @Value("${key}")
    private String key;//注：key为商户平台设置的密钥key
    private String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";//微信支付统一下单地址
    @Value("${notify_url}")
    private String notify_url;//回调地址

    @ApiOperation(value="用户发起支付" , notes="用户发起支付", response= Result.class , httpMethod = "POST")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token",value = "登录标识",required = true),
            @ApiImplicitParam(name = "userId",value = "用户id",required = true),
            @ApiImplicitParam(name = "amount",value = "充值金额",required = true),
    })
    @RequestMapping(value = "goPay", method = RequestMethod.POST)
    private Result goPay(HttpServletRequest request, @RequestBody RechargeReq req) throws Exception {
        if(Utils.isEmpty(req.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(req.getToken());
        if (null == userToken) {
            return Result.tokenInvalidResult();
        }
        if(Utils.isEmpty(req.getUserId())){
            return Result.failResult("参数userId不能为空");
        }
        if(Utils.isEmpty(req.getAmount())){
            return Result.failResult("参数amount不能为空");
        }
        User user = userService.selectById(req.getUserId());
        if(Utils.isEmpty(user)){
            return Result.failResult("用户不存在");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.DATE_All_KEY_STR);
        //充值订单号
        String out_trade_no = "CA"+simpleDateFormat.format(new Date());
        Recharge recharge = new Recharge();
        recharge.setUserId(user.getUserid());
        recharge.setAmount(req.getAmount());
        recharge.setBalance(user.getBalance());
        recharge.setPayType(1);
        recharge.setStatus(3);
        recharge.setCreateTime(new Date());
        boolean insert = rechargeService.insert(recharge);

        Map<String, String> result = new HashMap<String, String>();//返回给小程序端需要的参数
        if(insert){
            //金额元=paymentPo.getTotal_fee()*100
            String total_fee = String.valueOf(new BigDecimal(req.getAmount()+"").multiply(new BigDecimal(100)).intValue());
            //商品名称
            String body = "会员充值";
            //随机字符串
            String nonce_str = WXUtil.create_nonce_str();
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", appid);
            packageParams.put("mch_id", mchId);
            packageParams.put("nonce_str", nonce_str);//随机字符串
            packageParams.put("body", body);//商品描述
            packageParams.put("out_trade_no", out_trade_no);//商户订单号
            packageParams.put("total_fee", total_fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("notify_url", notify_url);//支付成功后的回调地址
            packageParams.put("trade_type", tradeType);//支付方式

            String prestr = WXUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = WXUtil.sign(prestr, key, "utf-8").toUpperCase();

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + appid + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + mchId + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + notify_url + "</notify_url>"
                    /*+ "<openid>" + paymentPo.getOpenid() + "</openid>"*/
                    + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                    + "<spbill_create_ip>" + WXUtil.getIp(request) + "</spbill_create_ip>" //终端ip
                    + "<total_fee>" + total_fee + "</total_fee>"
                    + "<trade_type>" + tradeType + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";

            log.info("=======>调试模式_统一下单接口 请求XML数据：" + xml);
            //调用统一下单接口，并接受返回的结果
            String res = WXUtil.httpRequest(payUrl, "POST", xml);
            log.info("=======>统一下单接口 返回XML数据：" + res);
            // 将解析结果存储在HashMap中
            Map map = WXUtil.doXMLParse(res);
            String return_code = (String) map.get("return_code");//返回状态码
            log.info("=======>统一下单接口 返回return_code数据：" + return_code);
            String prepay_id = null;
            if("SUCCESS".equals(return_code)){
                prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                result.put("nonceStr", nonce_str);
                result.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                result.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = WXUtil.sign(stringSignTemp, key, "utf-8").toUpperCase();
                result.put("paySign", paySign);
            }else {
                log.info("微信统一下单接口返回结果，return_code："+return_code + " ;return_msg:"+(String) map.get("return_msg"));
                return Result.failResult("微信统一下单接口返回结果，return_code："+return_code + " ;return_msg:"+(String) map.get("return_msg"));
            }
            result.put("appid", appid);
        }else {
            log.info("===========>数据插入数据库失败");
            return Result.failResult("系统错误，请联系管理员");
        }
        log.info("===========>返回小程序端数据：nonceStr："+result);
        return Result.successResult(result);
    }


    /**
     * 支付回调
     * @param request
     * @param response
     * @throws InterruptedException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value="/pay/notify")
    public synchronized void notify(HttpServletRequest request, HttpServletResponse response) throws InterruptedException{
        log.info("========>进入微信支付回调接口");
        String orderId = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();
            //sb为微信返回的xml
            String notityXml = sb.toString();
            String resXml = "";
            log.info("========>接收到的报文：" + notityXml);
            //解析xml
            Map map = WXUtil.doXMLParse(notityXml);
            //返回结果判断
            String returnCode = (String) map.get("return_code");
            log.info("========>回调return_code：" + returnCode);
            if("SUCCESS".equals(returnCode)){
                //验证签名是否正确
                Map<String, String> validParams = WXUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
                String validStr = WXUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
                String sign = WXUtil.sign(validStr, key, "utf-8").toUpperCase();//拼装生成服务器端验证的签名
                //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
                log.info("========>本地加密结果：" + sign);
                log.info("========================================================>");
                log.info("========>微信加密结果：" + map.get("sign"));
                if(sign.equals(map.get("sign"))){
                    /**此处添加自己的业务逻辑代码start**/
                    //TODO
                    //商户订单号
                    String out_trade_no = (String) map.get("out_trade_no");
                    //微信支付订单号
                    String transaction_id = (String) map.get("transaction_id");
                    //用户支付金额
                    String settlement_total_fee = (String) map.get("settlement_total_fee");
                    Wrapper<Recharge> wrapper = new EntityWrapper<>();
                    wrapper.eq("outTradeNo", out_trade_no);
                    Recharge recharge = rechargeService.selectOne(wrapper);
                    if(Utils.isEmpty(recharge)){
                        log.info("========>充值订单号:"+out_trade_no + ",不存在");
                    }else {
                        String total_fee = String.valueOf(new BigDecimal(recharge.getAmount()+"").multiply(new BigDecimal(100)).intValue());
                        if(!total_fee.equals(settlement_total_fee)){
                            log.info("========>订单中充值金额（单位：分）为："+total_fee +";用户实际支付金额（单位分）为："+settlement_total_fee);
                        }
                        recharge.setTransactionId(transaction_id);
                        recharge.setStatus(2);
                        recharge.setUpdateTime(new Date());
                        boolean update = rechargeService.updateById(recharge);
                        if(update){
                            log.info("========>数据库数据更新成功");
                        }else {
                            log.info("========>数据库数据更新失败");
                        }
                    }
                    /**此处添加自己的业务逻辑代码end**/

                    //通知微信服务器已经支付成功
                    resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                }

            }else{
                resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[FAIL]]></return_msg></xml>";
            }
            log.info("========>响应微信回调数据："+resXml);
            log.info("========>微信支付回调数据结束");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.info("=======================>微信回调接口异常："+e);
        }
    }

}
