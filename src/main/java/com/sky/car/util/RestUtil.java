package com.sky.car.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author skyween
 * @desc 网络请求封装类
 */
public class RestUtil {
	
	private static final Logger log = LoggerFactory.getLogger(RestUtil.class);
	
	private static RestTemplate rest = null;
	private static final String POST_DATA = ",post data:";
	private static final String REQUEST_URL = "request url:";
	private static final String SERVER_RETURNDATA = ",server returndata:";
	
	
	private RestUtil() {
		
	}
	
	static {
		if(rest==null) {
			int connectTimeout = 120000;
			int readTimeout = 180000;
			SimpleClientHttpRequestFactory simple = new SimpleClientHttpRequestFactory();
			simple.setConnectTimeout(connectTimeout);
			simple.setReadTimeout(readTimeout);
			rest = new RestTemplate(simple);
			rest.setErrorHandler(new RestResponseErrorHandler());
		}
	}
	
	public static <T> T getForObject(String url,Class<T> responseType, Object... uriVariables) {
		T t = rest.getForObject(url, responseType,uriVariables);
		if(log.isInfoEnabled()) {
			log.info(REQUEST_URL+url+SERVER_RETURNDATA+JSONObject.toJSONString(t));
		}
		return t;
	}
	
	public static <T> T getForObject(String url,JSONObject params ,Class<T> responseType, Object... uriVariables) {
		if(null ==params) {
			log.error("getForObject request data is null , must not null");
			return null;
		}
		T t = rest.getForObject(expandURL(url, params.keySet()), responseType, params,uriVariables);
		if(log.isInfoEnabled()) {
			log.info(REQUEST_URL+url+POST_DATA+JSONObject.toJSONString(params)+SERVER_RETURNDATA+JSONObject.toJSONString(t));
		}
		return t;
	}
	
	public static <T> T postForObject(String url,Object request,Class<T> responseType,Object... uriVariables) {
		if(null ==request) {
			log.error("postForObject request data is null , must not null");
			return null;
		}
		T t = rest.postForObject(url, request, responseType,uriVariables);
		if(log.isInfoEnabled()) {
			log.info(REQUEST_URL+url+POST_DATA+JSONObject.toJSONString(request)+SERVER_RETURNDATA+JSONObject.toJSONString(t));
		}
		return t;
	}
	
	public static <T> T postForObject(String url, JSONObject params, Class<T> responseType, Object... uriVariables) {
		if(null ==params) {
			log.error("postForObject request data is null , must not null");
			return null;
		}
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(params, requestHeaders);
		T t = rest.postForObject(url, requestEntity, responseType,uriVariables);
		if(log.isInfoEnabled()) {
			log.info(REQUEST_URL+url+POST_DATA+JSONObject.toJSONString(params)+SERVER_RETURNDATA+JSONObject.toJSONString(t));
		}
		return t;
	}
	
	public static <T> List<T> postForArray(String url,Object request, Class<T> responseType , Object ... uriVariables) {
		if(null ==request) {
			log.error("postForArray request data is null , must not null");
			return Collections.emptyList();
		}
		String jsonStr = rest.postForObject(url, request, String.class, uriVariables);
		List<T> list = JSONObject.parseArray(jsonStr, responseType);
		if(log.isInfoEnabled()) {
			log.info(REQUEST_URL+url+POST_DATA+JSONObject.toJSONString(request)+SERVER_RETURNDATA+JSONObject.toJSONString(list));
		}
		return list;
	}
	
	public static <T> List<T> postForArray(String url,JSONObject params, Class<T> responseType , Object ... uriVariables) {
		if(null ==params) {
			log.error("postForArray request data is null , must not null");
			return Collections.emptyList();
		}
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(params, requestHeaders);
		String jsonStr = rest.postForObject(url, requestEntity, String.class,uriVariables);
		List<T> list = JSONObject.parseArray(jsonStr, responseType);
		if(log.isInfoEnabled()) {
			log.info(REQUEST_URL+url+POST_DATA+JSONObject.toJSONString(params)+SERVER_RETURNDATA+JSONObject.toJSONString(list));
		}
		return list;
	}
	
	private static String expandURL(String url, Set<?> keys) {
		StringBuilder sb = new StringBuilder(url);
		if (url.contains("?")||url.contains("&")) {
			sb.append("&");
		} else {
			sb.append("?");
		}
		for (Object key : keys) {
			sb.append(key).append("=").append("{").append(key).append("}").append("&");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
	
}
