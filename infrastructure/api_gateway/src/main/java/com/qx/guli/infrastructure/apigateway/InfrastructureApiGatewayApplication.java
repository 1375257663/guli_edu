package com.qx.guli.infrastructure.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Classname InfrastructureApiGatewayApplication
 * @Description 启动类
 * @Date 2020/6/28 22:05
 * @Created by 卿星
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
// nacos引入（可以不写）
@EnableDiscoveryClient
public class InfrastructureApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApiGatewayApplication.class,args);
    }
}
