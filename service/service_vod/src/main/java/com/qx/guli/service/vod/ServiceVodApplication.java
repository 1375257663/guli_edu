package com.qx.guli.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Classname ServiceVodApplication
 * @Description TODO
 * @Date 2020/6/16 20:33
 * @Created by 卿星
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)// 取消数据源自动配置
@ComponentScan({"com.qx.guli"})
@EnableDiscoveryClient
@EnableSwagger2
public class ServiceVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class);
    }
}
