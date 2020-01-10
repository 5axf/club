package com.sky.car.config;

//swagger2的配置文件，在项目的启动类的同级文件建立

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
//@EnableSwagger2
//@Profile({"dev","prod"})
public class SwaggerConfig {
//swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
  @Bean
  public Docket createRestApi() {
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(apiInfo())
              .select()
              //为当前包路径
              .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//              .apis(RequestHandlerSelectors.basePackage("com.sky.car.business.api.admin"))
              .paths(PathSelectors.any())
              .build();
  }
  //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
  private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
              //页面标题
              .title("天域云平台 微服务 Restful API")
              //创建人
              .contact(new Contact("刘鹏", "https://www.tianyugolf.com", ""))
              //版本号
              .version("2.0")
              //描述
              .description(
            		  "API文档包含小程序接口、后台管理接口\n"+
            	      "数据返回格式（非分页）\n"
          			+ "{\n" + 
          			"\"code\": 0,\n" + 
          			"\"msg\": \"success\",\n" + 
          			"\"data\": { …json… }" + 
          			"}\n"
            		  )
              .build();
  }


}