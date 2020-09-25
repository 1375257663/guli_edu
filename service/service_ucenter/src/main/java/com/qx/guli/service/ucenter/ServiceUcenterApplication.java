package com.qx.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Classname ServiceUcenterApplication
 * @Description TODO
 * @Date 2020/6/23 16:39
 * @Created by 卿星
 */
@ComponentScan(basePackages = "com.qx.guli")
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceUcenterApplication {

  public static void main(String[] args) {
      SpringApplication.run(ServiceUcenterApplication.class,args);
  }

}
