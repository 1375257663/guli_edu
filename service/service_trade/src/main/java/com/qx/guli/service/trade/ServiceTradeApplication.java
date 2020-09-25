package com.qx.guli.service.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Classname ServiceTradeApplication
 * @Description 启动类
 * @Date 2020/6/26 11:29
 * @Created by 卿星
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.qx.guli"})
public class ServiceTradeApplication {

  public static void main(String[] args) {
      SpringApplication.run(ServiceTradeApplication.class,args);
  }
}
