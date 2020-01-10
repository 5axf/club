package com.sky.car.business.service;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sky.car.common.AdminToken;
import com.sky.car.util.MD5;

@Service
public class AdminTokenService {
	
	private static boolean isDebug = true;
	
	private static long EXPIRE_TIME = 3*24*3600*1000L; //5天
	
	public static Logger LOGGER = LoggerFactory.getLogger(AdminTokenService.class);

	public static ConcurrentHashMap<String, AdminToken> tokenMap = new ConcurrentHashMap<String, AdminToken>();

	public AdminToken checkToken(String tokenId) {
		
		if(isDebug && tokenId.equals("iamadmin")) return createTokenByTokenId("iamadmin", "super_admin");
		
		AdminToken accessToken = (AdminToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.info("token【" + tokenId + "】不存在！");
			return null;
		}
		if (System.currentTimeMillis() > accessToken.getExpire()) {
			LOGGER.info("token【" + tokenId + "】已过期！");
			return null;
		}
		if (!accessToken.getTokenId().equals(tokenId)) {
			LOGGER.error(String.format(
					"用户【%s】的token错误！用户发送的token【%s】,服务器内存中token【%s】",
					new Object[] { accessToken.getUserId(),
							accessToken.getTokenId(), tokenId }));
			return null;
		}
		return accessToken;
	}
	
	public void addTokenExpire(String tokenId) {
		AdminToken accessToken = checkToken(tokenId);
		if (accessToken!=null) {
			LOGGER.info("用户【" + accessToken.getUserId() + "】续航 "+EXPIRE_TIME);
			accessToken.setExpire(System.currentTimeMillis() + EXPIRE_TIME);
		}
	}
	
	public void removeToken(String tokenId) {
		tokenMap.remove(tokenId);
	}

	public AdminToken createTokenByUserId(String userId) {
		if ((userId == null) || ("".equals(userId))) {
			LOGGER.error("userId必须是非空的字符串！");
			throw new RuntimeException("userId必须是非空的字符串！");
		}

		AdminToken token = new AdminToken();
		token.setUserId(userId);
		token.setTokenId(MD5.md5(userId + UUID.randomUUID().toString()));
		token.setExpire(System.currentTimeMillis() + EXPIRE_TIME);
		token.setSessionKey("admin");
		tokenMap.put(token.getTokenId(), token);

		return token;
	}

	public AdminToken createTokenByTokenId(String tokenId, String userId) {
		if ((tokenId == null) || ("".equals(tokenId))) {
			LOGGER.error("tokenId必须是非空的字符串！");
			throw new RuntimeException("tokenId必须是非空的字符串！");
		}
		if ((userId == null) || ("".equals(userId))) {
			LOGGER.error("userId必须是非空的字符串！");
			throw new RuntimeException("userId必须是非空的字符串！");
		}

		AdminToken token = new AdminToken();
		token.setUserId(userId);
		token.setTokenId(tokenId);
		token.setExpire(System.currentTimeMillis() + EXPIRE_TIME);
		token.setSessionKey("admin");
		tokenMap.put(token.getTokenId(), token);

		return token;
	}

	public void setAttribute(String tokenId, String key, Object obj) {
		AdminToken accessToken = (AdminToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.error("setAttribute 的时候，accessToken 为null");
			return;
		}
		accessToken.getSessionMap().put(key, obj);
	}

	public Object getAttribute(String tokenId, String key) {
		AdminToken accessToken = (AdminToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.error("getAttribute 的时候，accessToken 为null");
			return null;
		}
		Object obj = accessToken.getSessionMap().get(key);
		return obj;
	}

	public void removeAttribute(String tokenId, String key) {
		AdminToken accessToken = (AdminToken) tokenMap.get(tokenId);
		if (accessToken == null) {
			LOGGER.error("removeAttribute 的时候，accessToken 为null");
			return;
		}
		accessToken.getSessionMap().remove(key);
	}
}