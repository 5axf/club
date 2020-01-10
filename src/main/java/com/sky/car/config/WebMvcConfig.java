
package com.sky.car.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sky.car.interceptor.AdminAuthorizationInterceptor;
import com.sky.car.interceptor.UserAuthorizationInterceptor;

@Configuration
@Component
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Value("${file.staticAccessPath}")
    private String staticAccessPath;
    @Value("${file.uploadPath}")
    private String uploadPath;
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
 
        registry.addViewController("/").setViewName("forward:/index.html");
 
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
 
        super.addViewControllers(registry);
 
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		

        //registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        
        // 解决 SWAGGER 404报错
        //registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        //registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        //访问静态文件
        //registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + uploadFolder);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
        		 getUserAuthorizationInterceptor())
        		 .addPathPatterns("/**")
        		 //.excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/error/","/error")
        		 //.excludePathPatterns("doc.html")
        		 .excludePathPatterns("/myTest/**","/wx/**");
        
        registry.addInterceptor(getAdminAuthorizationInterceptor())
        		  .addPathPatterns("/**")
        		  .excludePathPatterns("/admin/**");
        
        super.addInterceptors(registry);
    }
    
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowCredentials(true)
		.allowedMethods("GET","POST","DELETE","PUT")
		.maxAge(3600);
	}
	
	@Bean
	public HandlerInterceptor getUserAuthorizationInterceptor() {
		return new UserAuthorizationInterceptor();
	}
	
	@Bean
	public HandlerInterceptor getAdminAuthorizationInterceptor() {
		return new AdminAuthorizationInterceptor();
	}

}
