package com.winbaoxian.testng.platform.component.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@Configuration
@EnableCaching
@Slf4j
public class RedisCacheConfiguration {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public CacheManager cacheManager() {
        log.info("RedisCacheConfiguration.cacheManager : 实例化");
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        cacheManager.setDefaultExpiration(60 * 5);
        return cacheManager;
    }

}
