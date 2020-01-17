package com.sky.car.myInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.sky.car.business.service.AdminTokenService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

/**
 * @author lp
 * @date 2020-01-17 10:23
 */
public class RepeatedlyReadInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RepeatedlyReadInterceptor.class);

    private AdminTokenService adminTokenService;
    public RepeatedlyReadInterceptor(AdminTokenService adminTokenService){
        this.adminTokenService = adminTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result result = new Result();
        String method = request.getMethod();
        if("OPTIONS".equals(method)){
//            System.out.println("OPTIONS请求直接通过==============>:");
            return true;
        }
        RepeatedlyReadRequestWrapper requestWrapper;
        if (request instanceof RepeatedlyReadRequestWrapper) {
            requestWrapper = (RepeatedlyReadRequestWrapper) request;
            if(Utils.isNotEmpty(requestWrapper)){
                String string = getBodyString(requestWrapper);
                if("".equals(string)){
                    result.setCode(1);
                    result.setMsg("缺少token,请登录!");
                    writeResponse(response, result);
                    return false;
                }
                logger.info("请求Body: {} ", string );
                JSONObject jsonObject = JSONObject.parseObject(getBodyString(requestWrapper));
                String token = jsonObject.getString("token");

                if (Utils.isEmpty(token)) {
//                    LOG.warn(String.format("来自IP[%s]请求缺少tokenId,请求非法！", RequestUtil.getPosterIp(request)));
                    result.setCode(1);
                    result.setMsg("缺少token,请登录!");
                    writeResponse(response, result);
                    return false;
                }
                AdminToken adminToken = adminTokenService.checkToken(token);
                if (Utils.isEmpty(adminToken)){
                    result.setCode(1);
                    result.setData(new Object());
                    result.setMsg("鉴权服务失败！");
                    writeResponse(response, result);
                    return false;
                }
            }
        }
        // 默认记录后台接口请求日志记录
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        logger.info(String.format("请求参数, url: %s, method: %s, uri: %s, params: %s ", url, method, uri, queryString));
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RepeatedlyReadRequestWrapper requestWrapper;
        if (request instanceof RepeatedlyReadRequestWrapper) {
            // 测试再次获取Body start....
            requestWrapper = (RepeatedlyReadRequestWrapper) request;
            if(Utils.isNotEmpty(requestWrapper)){
//                logger.info("请求Body: {} ", getBodyString(requestWrapper));
            }
            // 测试再次获取Body end....
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 获取请求Body
     *
     * @param request
     *
     * @return
     */
    public static String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = cloneInputStream(request.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * Description: 复制输入流</br>
     *
     * @param inputStream
     *
     * @return</br>
     */
    public static InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }

    private void writeResponse(HttpServletResponse response, Result result) {
        PrintWriter out = null;
        try {
            //解决跨域问题
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.addHeader("Access-Control-Max-Age", "3600");
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");

            out = response.getWriter();
            JSONObject res = new JSONObject();
            res.put("code", result.getCode());
            res.put("data", result.getData());
            res.put("msg", result.getMsg());
            out.append(res.toJSONString());
            out.flush();
        } catch (IOException e) {
//            LOG.error("回写客户端响应异常【" + result.toString() + "】", e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }


}

