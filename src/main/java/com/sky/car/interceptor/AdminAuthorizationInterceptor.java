package com.sky.car.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sky.car.business.service.AdminTokenService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.Result;
import com.sky.car.common.UserSession;
import com.sky.car.util.JSONUtil;
import com.sky.car.util.RequestUtil;
import com.sky.car.util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {
	
	public static boolean isDev = true;
	
    protected Log LOG = LogFactory.getLog(getClass());
    
   @Autowired  AdminTokenService adminTokenService;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
    	
	    	if(isDev) {
	    		return true;
	    	}

    		//占位符不需要if判断
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("客户端[%s]-认证拦截器处理开始,请求参数【%s】" ,
                    new Object[]{RequestUtil.getPosterIp(request), RequestUtil.getRequestParam(request)}));
        }
        String data = "";
        Result result = new Result();
        String token = request.getParameter("token");
        String sign = request.getParameter("sign");
        if (Utils.isEmpty(token)) {
		    LOG.warn(String.format("来自IP[%s]请求缺少tokenId,请求非法！", RequestUtil.getPosterIp(request)));
            result.setCode(103);
            result.setMsg("缺少tokenId,请登录!");
            writeResponse(response, result);
            return false;
        }

        try {
        	
        	AdminToken adminToken = adminTokenService.checkToken(token);
        	
            //服务器user超时验证
            if (adminToken==null) {
                LOG.info(String.format("来自客户端[%s]-user已过期！", new Object[]{RequestUtil.getPosterIp(request) }));
                result.setCode(105);
                result.setMsg("用户未登录,请登录！");
                writeResponse(response, result);
                return false;
            }
            
            request.setAttribute("token", adminToken.getTokenId());
            request.setAttribute("userid", adminToken.getUserId());
            
        } catch (Exception e) {
            LOG.error(String.format("鉴权服务异常【{sign:%s,verifyStr:%s}】_exception:%s" , 
                    new Object[]{sign, data, e.getMessage()}));
            result.setCode(201);
            result.setData(new Object());
            result.setMsg("鉴权服务失败！");
            writeResponse(response, result);
            return false;
        }
        //非占位符需要if判断
        if (LOG.isDebugEnabled()) {
            LOG.debug("客户端[" + RequestUtil.getPosterIp(request) + "]-已通过认证拦截器结束");
        }
        return true;
    }

    private boolean checkToken(UserSession userSession , String tokenId) {
    	String mTokenId = null;
    	if(userSession!=null){
    		mTokenId = userSession.getTokenId();
    	}
    	if(Utils.isEmpty(tokenId) || Utils.isEmpty(mTokenId)){
    		return false;
    	}
    	
    	if(tokenId.equals(mTokenId)){
    		return true;
    	}
    	
    	return false;
	}

	@Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
	

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    private void writeResponse(HttpServletResponse response, Result result) {
        PrintWriter out = null;
        try {
            response.setContentType("text/json");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();

            out.write(JSONUtil.toJSON(result));
            out.flush();

        } catch (IOException e) {
            LOG.error("回写客户端响应异常【" + result.toString() + "】", e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    public static void main(String[] args) {
    	
    }

}
