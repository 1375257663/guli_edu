package com.qx.guli.service.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Classname ServiceSmsApplication
 * @Description TODO
 * @Date 2020/6/23 11:53
 * @Created by 卿星
 */

@ComponentScan(basePackages = {"com.qx.guli"})
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServiceSmsApplication {

  public static void main(String[] args) {
      SpringApplication.run(ServiceSmsApplication.class,args);
  }
}
