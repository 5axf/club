package com.sky.car.util;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	
	private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	public static String toJSON(Object value) {
		StringWriter writer = new StringWriter();
		try {
			objectMapper.writeValue(writer, value);
		} catch (Exception e) {
			log.error("JSONUtil.toJSON",e);
		}
		return writer.toString();
	}
	
	public static <T> T parse2Bean(String jsonString , Class<T> clazz) {
		try {
			return objectMapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			log.error("JSONUtil.parse2Bean",e);
		}
		return null;
	}
	
	public static <T> T parse2Bean(String jsonString , TypeReference<T> type) {
		try {
			return objectMapper.readValue(jsonString, type);
		} catch (Exception e) {
			log.error("JSONUtil.parse2Bean",e);
		}
		return null;
	}

}
