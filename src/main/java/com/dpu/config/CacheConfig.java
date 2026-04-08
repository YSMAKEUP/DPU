
package com.dpu.config;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary  // 기본 CacheManager로 Caffeine 사용
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
        );
        return manager;
    }

    // Redis CacheManager는 RedisConfig.java에서 별도 등록 예정
    // @Bean public CacheManager redisCacheManager(...) { ... }
}