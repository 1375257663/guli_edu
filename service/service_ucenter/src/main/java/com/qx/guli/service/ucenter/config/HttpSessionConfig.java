package com.qx.guli.service.ucenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Classname HttpSessionConfig
 * @Description HttpSessionConfig配置类
 * @Date 2020/6/25 11:42
 * @Created by 卿星
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

    // 重写CookieSerializer
    @Bean
    public CookieSerializer cookieSerializer(){
        // 创建Cookie序列化对象
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        // 将SpringSession提供的SESSIOONID修改为JSESSIONID
        cookieSerializer.setCookieName("JSESSIONID");
        // CookiePath设为根路径
        cookieSerializer.setCookiePath("/");
        // 配置相关正则表达式，达到同父域下单点登录效果
        cookieSerializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");

        return cookieSerializer;
    }


}
