
package com.sky.car.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);

    public CookieUtil() {
    }

    public static void writeCookie(HttpServletResponse response, String key, String value) {
        writeCookie(response, key, value, 1209600, "/");
    }

    public static void writeCookie(HttpServletResponse response, String key, String value, int maxAge, String path) {
        try {
            Cookie cookie = new Cookie(key, value);
            cookie.setMaxAge(maxAge);
            cookie.setPath(path);
            response.addCookie(cookie);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cc = request.getCookies();
        if(cc == null) {
            return null;
        } else {
            Cookie[] arr$ = cc;
            int len$ = cc.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Cookie cookie = arr$[i$];
                if(cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }

            return null;
        }
    }

    public static void removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cc = request.getCookies();
        if(cc == null || cc.length < 1) {
            LOGGER.error("请求里cookie为null");
        }

        Cookie[] arr$ = cc;
        int len$ = cc.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Cookie cookie = arr$[i$];
            if(cookie.getName().equals(name)) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

    }
}
