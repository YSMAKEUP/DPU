package com.dpu.config;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheStatsLogger {

    private final CacheManager cacheManager;

    @Scheduled(fixedDelay = 60000) // 1분마다
    public void logCacheStats() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                CacheStats stats = cache.getNativeCache().stats();
                log.info("[캐시 통계] {} - 히트율: {}, 히트: {}, 미스: {}",
                        cacheName,
                        String.format("%.1f%%", stats.hitRate() * 100),
                        stats.hitCount(),
                        stats.missCount());
            }
        });
    }
}