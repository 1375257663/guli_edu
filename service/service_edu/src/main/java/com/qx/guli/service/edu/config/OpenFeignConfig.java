package com.qx.guli.service.edu.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname OpenFeignConfig
 * @Description TODO
 * @Date 2020/6/8 20:41
 * @Created by 卿星
 */
@Configuration
public class OpenFeignConfig {

    /**
     * NONE：默认级别，不显示日志
     * BASIC：仅记录请求方法、URL、响应状态及执行时间
     * HEADERS：除了BASIC中定义的信息之外，还有请求和响应头信息
     * FULL：除了HEADERS中定义的信息之外，还有请求和响应正文及元数据信息
     * @return
     */
    @Bean
    Logger.Level feigneLoggerLevel(){
        return Logger.Level.FULL;
    }

}
