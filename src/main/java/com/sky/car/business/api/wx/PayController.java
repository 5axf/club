package com.sky.car.business.api.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sky.car.business.entity.recharge.Recharge;
import com.sky.car.business.entity.recharge.RechargeReq;
import com.sky.car.business.entity.user.User;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.business.service.order.OrderService;
import com.sky.car.business.service.recharge.RechargeService;
import com.sky.car.business.service.user.UserService;
import com.sky.car.common.Result;
import com.sky.car.common.UserToken;
import com.sky.car.config.WxPayConfig;
import com.sky.car.util.IpUtil;
import com.sky.car.util.PayUtil;
import com.sky.car.util.StringUtils;
import com.sky.car.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wx/pay/")
@Api(tags = { "后台管理-订单管理接口" })
public class PayController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RechargeService rechargeService;

    @ApiOperation(value="结算" , notes="结算" ,
            response= Result.class , httpMethod = "POST")
    @RequestMapping(value = "toRecharge", method = RequestMethod.POST)
    public Result toRecharge(HttpServletRequest request, @RequestBody RechargeReq body){
        if (Utils.isEmpty(body.getToken(),body.getAmount())){
            return Result.failResult("参数为空");
        }
        if(Utils.isEmpty(body.getToken())) {
            return Result.failResult("token为空");
        }
        UserToken userToken = userTokenService.checkToken(body.getToken());
        if (Utils.isEmpty(userToken)) {
            return Result.tokenInvalidResult();
        }
        User user = userService.selectById(userToken.getUserId());
        if (!StringUtils.isRecharge(body.getAmount())){
            return Result.failResult("金额格式错误");
        }
        // 充值金额
        String amount = body.getAmount();
        //充值单号
        String outTradeNo = "TY" + System.currentTimeMillis();
        // 插入充值记录
        Recharge recharge = new Recharge();
        recharge.setOutTradeNo(outTradeNo);
        recharge.setUserId(user.getUserid());
        recharge.setAmount(new BigDecimal(amount));
        recharge.setPayType(1);
        recharge.setBalance(user.getBalance());
        recharge.setStatus(2); // 默认无效
        recharge.setCreateTime(new Date());
        rechargeService.insert(recharge);

//        BigDecimal decimal = new BigDecimal(amount);
//        BigDecimal fen = new BigDecimal("100");
//        BigDecimal price = decimal.multiply(fen);

        Double aDouble = Double.valueOf(amount);
        double temp = aDouble * 100;
        int price = (int) temp;
        try {

            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);
            String name = "用户余额充值";
            //获取客户端的ip地址
            String spbill_create_ip = IpUtil.getIpAddr(request);
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", name);
            packageParams.put("out_trade_no", recharge.getOutTradeNo()+ "");//商户订单号,自己的订单ID
            packageParams.put("total_fee", price + "");//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);//支付成功后的回调地址
            packageParams.put("trade_type", WxPayConfig.TRADETYPE);//支付方式
            packageParams.put("openid",  user.getOpenid());//用户的openID，自己获取
            packageParams.put("sign_type",  WxPayConfig.SIGNTYPE);//

            // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String prestr = PayUtil.createLinkString(packageParams);

            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                    + "<body><![CDATA[" + name + "]]></body>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                    + "<openid>" + user.getOpenid() + "</openid>"
                    + "<out_trade_no>" + recharge.getOutTradeNo() + "</out_trade_no>"
                    + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                    + "<total_fee>" + price + "</total_fee>"//支付的金额，单位：分
                    + "<trade_type>" + WxPayConfig.TRADETYPE + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "<sign_type>" + WxPayConfig.SIGNTYPE + "</sign_type>"
                    + "</xml>";

            //调用统一下单接口，并接受返回的结果
            String result = PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);
            // 将解析结果存储在HashMap中
            Map map = PayUtil.doXMLParse(result);

            String return_code = (String) map.get("return_code");//返回状态码
            String result_code = (String) map.get("result_code");//返回状态码
            String return_msg = (String) map.get("return_msg");//返回信息

            Map<String, Object> response = new HashMap<String, Object>();//返回给小程序端需要的参数

            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + WxPayConfig.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key, "utf-8").toUpperCase();

                response.put("paySign", paySign);
            }
            response.put("appid", WxPayConfig.appid);
            return Result.successResult(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //这里是支付回调接口，微信支付成功后会自动调用
    @RequestMapping(value = "/wxNotify", method = RequestMethod.POST)
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("============================================回调了======================================");

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";

        Map map = PayUtil.doXMLParse(notityXml);
        String returnCode = (String) map.get("return_code");


        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            String prestr = PayUtil.createLinkString(validParams);
            String mapstr = PayUtil.createLinkString(map);
            String sign = (String) map.get("sign");
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if (PayUtil.verify(prestr, sign, WxPayConfig.key, "utf-8")) {
                //注意要判断微信支付重复回调，支付成功后微信会重复的进行回调
                String outTradeNo = validParams.get("out_trade_no");
                String transactionId = validParams.get("transaction_id");
                Wrapper<Recharge> wrapper = new EntityWrapper<>();
                wrapper.eq("outTradeNo",outTradeNo);
                Recharge recharge = rechargeService.selectOne(wrapper);
                if(recharge.getStatus() == 2){
                    // 返回的总金额
                    System.out.println("============================================返回的总金额======================================");
                    String total_fee = validParams.get("total_fee");
                    BigDecimal totalFee = new BigDecimal(total_fee);
                    BigDecimal temp = recharge.getAmount();
                    BigDecimal amount = temp.multiply(new BigDecimal("100"));
                    if (totalFee.compareTo(amount) != 0){
                        resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                                + "<return_msg><![CDATA[金额不一致]]></return_msg>" + "</xml> ";
                    }else {
                        recharge.setUpdateTime(new Date());
                        recharge.setStatus(1);
                        recharge.setTransactionId(transactionId);
                        rechargeService.updateById(recharge);
                        User user = userService.selectById(recharge.getUserId());
                        BigDecimal oldBalance = user.getBalance();
                        BigDecimal newBalance = oldBalance.add(temp);
                        user.setBalance(newBalance);
                        user.setUpdateTime(new Date());
                        userService.updateById(user);
                    }
                    /**此处添加自己的业务逻辑代码end**/
                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![充值成功]></return_msg>" + "</xml> ";
                }else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[充值已结束]]></return_msg>" + "</xml> ";
                }
            }else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[签名校验失败]]></return_msg>" + "</xml> ";
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[充值失败]]></return_msg>" + "</xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }



}
