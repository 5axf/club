package com.sky.car.util;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * 请求工具
 */
public class RequestUtil {

    /**
     * 获取request参数集合
     * @param request
     * @return
     */
    public static Map<String, Object> getRequestParam(HttpServletRequest request) {
        Map<String, Object> parameters = new WeakHashMap<String, Object>();
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String  name = (String) e.nextElement();
            String[] v = request.getParameterValues(name);
            if (v.length == 1) {
                if (v[0]!=null && !v[0].equals("")) {
                    parameters.put(name, v[0]);
                }
            } else {
                parameters.put(name, v);
            }
        }
        
        Object userId = request.getAttribute("userid");
        if(null!=userId) {
        	parameters.put("USERID", userId);
        }
        
        return parameters;
    }
    
	public static String getPosterIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if ((ip != null) && (ip.length() != 0)) {
			while ((ip != null) && (ip.equals("unknow"))) {
				ip = request.getHeader("x-forworded-for");
			}
		}

		if ((ip == null) || (ip.length() == 0)
				|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0)
				|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ((ip == null) || (ip.length() == 0)
				|| ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
    
}
