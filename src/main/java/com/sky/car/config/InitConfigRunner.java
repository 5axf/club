package com.sky.car.config;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sky.car.util.Utils;

@Component
public class InitConfigRunner implements ApplicationRunner{
	
	@Autowired AppConfig appConfig;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (Utils.isNotEmpty(appConfig.uploadPath)) {
			File file = new File(appConfig.uploadPath);
			if (!file.exists()) {
				file.mkdirs();
				file.setWritable(true,false);
			}
		}
	}

}
