package com.qx.guli.service.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Classname ServiceStatisticsApplication
 * @Description TODO
 * @Date 2020/6/29 16:34
 * @Created by 卿星
 */
@ComponentScan(basePackages = "com.qx.guli")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling // 启用任务调度功能
public class ServiceStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceStatisticsApplication.class, args);
    }
}
