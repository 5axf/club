package com.sky.car.myInterceptor;

import com.sky.car.business.service.AdminTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author lp
 * @date 2020-01-17 10:24
 */
@Configuration
public class WebMvcConfig2 extends WebMvcConfigurerAdapter {

    @Autowired
    AdminTokenService adminTokenService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RepeatedlyReadInterceptor(adminTokenService))
                .addPathPatterns("/admin/**") //拦截所有请求
                .excludePathPatterns("/admin/account/**")
                .excludePathPatterns("/admin/upload/**");//对应的不拦截的请求
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean repeatedlyReadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        RepeatedlyReadFilter repeatedlyReadFilter = new RepeatedlyReadFilter();
        registration.setFilter(repeatedlyReadFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","DELETE","PUT")
                .maxAge(3600);
    }

}

