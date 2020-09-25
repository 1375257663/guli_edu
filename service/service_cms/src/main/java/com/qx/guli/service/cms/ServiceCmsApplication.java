package com.qx.guli.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Classname ServiceCmsApplication
 * @Description TODO
 * @Date 2020/6/21 17:05
 * @Created by 卿星
 */
@ComponentScan({"com.qx.guli"} )
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ServiceCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsApplication.class, args);
    }

}
