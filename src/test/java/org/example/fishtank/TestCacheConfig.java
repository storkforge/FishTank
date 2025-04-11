package org.example.fishtank;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Use a simple CacheManager that doesn't cache anything
        return new ConcurrentMapCacheManager();
    }
}
