package com.patrykdankowski.financeflock.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
 class CacheService {

    CacheService(final CaffeineCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private final CaffeineCacheManager cacheManager;

    public Map<Object, Object> cachedObjects(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        Map<Object, Object> content = new HashMap<>();
        if (cache != null) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                    (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache.getNativeCache();
            nativeCache.asMap().forEach(content::put);
        }
        return content;
    }
}
