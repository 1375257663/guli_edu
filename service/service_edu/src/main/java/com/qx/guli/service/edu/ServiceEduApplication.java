package com.qx.guli.service.edu;

import com.qx.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Classname SpringMainClass
 * @Description TODO
 * @Date 2020/6/2 22:30
 * @Created by 卿星
 */
//@CrossOrigin // 不能设置在主启动类上，设置在启动类上controller层不会起作用
@SpringBootApplication
@ComponentScan(basePackages = {"com.qx.guli"})
//@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceEduApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class);
    }
}
