package com.qx.guli.service.base.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

/**
 * @Classname RedisConfig
 * @Description Redis配置类
 * @Date 2020/6/22 21:23
 * @Created by 卿星
 */
@EnableCaching
@Configuration
public class RedisConfig {

    /**
     * 自定义一个序列化器，避免使用jdk自带的序列化器，更方便的操作实例化对象
     * @param lettuceConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        // 创建RedisTemplate对象
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        // 设置redis自定义序列化配置
        // 设置value序列化方式
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 设置key序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置连接池
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);

        return redisTemplate;
    }

    /**
     * 配置缓存策略
     * @param lettuceConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory){
        // 设置规则
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置过期时间10分钟
                .entryTtl(Duration.ofSeconds(600))
                // 配置序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                // 空值时不使用
                .disableCachingNullValues();
        // 设置CacheManger
        RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
        return cacheManager;
    }

}
