package com.sky.car.business.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sky.car.business.service.AdminTokenService;
import com.sky.car.business.service.ShutdownContext;
import com.sky.car.business.service.UserTokenService;
import com.sky.car.common.AdminToken;
import com.sky.car.common.UserToken;

@Component
@Configuration
@EnableScheduling
public class TokenScheduleTask {
	
	@Autowired ShutdownContext shutdownContext ;
	
    @Scheduled(cron = "0/60 * * * * ?")
    //或直接指定时间间隔，例如：60秒
    //@Scheduled(fixedRate=60*1000)
    private void cleanUserTokenTasks() {
    	
        	for (UserToken token : UserTokenService.tokenMap.values())
			if (token.getExpire() < System.currentTimeMillis()) {
				UserTokenService.tokenMap.remove(token.getTokenId());
			}
    }
    
    @Scheduled(cron = "0/60 * * * * ?")
    //或直接指定时间间隔，例如：60秒
    //@Scheduled(fixedRate=60*1000)
    private void cleanAdminTokenTasks() {
    	
        	for (AdminToken token : AdminTokenService.tokenMap.values())
			if (token.getExpire() < System.currentTimeMillis()) {
				AdminTokenService.tokenMap.remove(token.getTokenId());
			}
    }

}
