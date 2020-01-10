package com.sky.car.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

@Configuration
@ComponentScan(basePackages = { "com.sky.car" })
public class ServerConfiguration {
    
    @Bean
    public PaginationInterceptor paginationInterceptor() {
    		return new PaginationInterceptor();
    }
    
}