package com.sky.car.config;


import com.sky.car.interceptor.ParamInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ParamConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new ParamInterceptor())//添加拦截器
//                .addPathPatterns("/admin/**") //拦截所有请求
//                .excludePathPatterns("/admin/account/**")
//                .excludePathPatterns("/admin/upload/**");//对应的不拦截的请求
    }

}
