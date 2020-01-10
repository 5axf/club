package com.sky.car.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class AppConfig {
    
	@Value("${file.uploadPath}")
	public String uploadPath;
	
	@Value("${file.allowExtNames}")
	public String allowExtNames;
}
