package com.sky.car.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.car.business.entity.wxMessage.WxMssVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;

public class WXUtil {
    /**
     * 获取随机字符串
     */
    public static String create_nonce_str() {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.replaceAll( "\\-","").toUpperCase();
    }
    /**
     * 获取时间戳
     */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Requested-For");
        if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (Utils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0];
    }

    /**
     * 获取 AccessToken
     * @param appid
     * @param secret
     * @return
     */
    public static String getAccessToken(String appid,String secret) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("APPID", appid);  //
        params.put("APPSECRET", secret);  //
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}", String.class, params);
        String body = responseEntity.getBody();
        JSONObject object = JSON.parseObject(body);
        String Access_Token = object.getString("access_token");
        String expires_in = object.getString("expires_in");
//        System.out.println("有效时长expires_in：" + expires_in);
        return Access_Token;
    }

    /**
     * 推送订阅消息
     * @param token token
     * @param wxMssVo 消息实体类
     * @return
     */
    public static String postForEntity(String token, WxMssVo wxMssVo){
        //推送订阅消息
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+token;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, wxMssVo, String.class);
        return responseEntity.getBody();
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    /**
     * 签名字符串
     *  @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    private static boolean isValidChar(char ch) {
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
            return true;
        if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
            return true;// 简体中文汉字编码
        return false;
    }
    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    /**
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方法
     * @param outputStr 参数
     */
    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
        // 创建SSLContext
        StringBuffer buffer = null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //往服务器端写内容
            if(null !=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
    public static String urlEncodeUTF8(String source){
        String result=source;
        try {
            result=java.net.URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws IOException
     */
    public static Map doXMLParse(String strxml) throws Exception {
        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }
    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    /**
     * str转Inputstream（字节）
     * @param str
     * @return
     */
    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }



    /**
     * 解密用户电话
     * @param keyStr sessionkey
     * @param ivStr  ivData
     * @param encDataStr 带解密数据
     * @return
     * @throws Exception
     * @author pangxianhe
     * @date 2018年10月22日
     */
    public static String decrypt2(String keyStr, String ivStr, String encDataStr)throws Exception {

        byte[] encData = Base64Util.decode(encDataStr);
        byte[] iv =Base64Util.decode(ivStr);
        byte[] key = Base64Util.decode(keyStr);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return new String(cipher.doFinal(encData),"UTF-8");
    }



    public static void main(String[] args) {
        String keyStr = "j/BtVFtBaff6lU0GM4CEfQ==";
        String ivStr = "EV3Ww8z+0XUsV+BeoVtg0Q==";
        String encDataStr = "jIlB8bMKt6YWtdf7mPN2fbAmxIy4i4ll4D/CwnZyognKDIHL/Ui3o7/CRJxXQMR9dfB1FshARkHtFSEe/lhdguSeE/ToCejX39K4/pwgBGuwPDvLb9Yqqb/Q0V0nRMi5XYwX90ZG/w2mAYrgWLO1mLpy6ydOVorW1kPf3YF27Y2nQQ72C/tdK4T+mvs5k2Yrf77GS7jO5Z6ud2CdQmPysw==";
        try {
            String decrypt = decrypt2(keyStr, ivStr, encDataStr);
            JSONObject jsonObject = JSON.parseObject(decrypt);
            String phoneNumber = jsonObject.getString("phoneNumber");
            System.out.printf("============>:"+phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
