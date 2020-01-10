package com.sky.car.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.sky.car.business.*")
public class DataSourceConfig {

}
