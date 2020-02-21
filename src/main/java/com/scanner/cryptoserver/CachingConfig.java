package com.scanner.cryptoserver;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
/*
    Configure caching. The caching is necessary to prevent too many calls to the exchanges.
    The exchanges limit the amount of data that can be retrieved, therefore, caching helps
    to prevent too much data being retrieved.
 */
public class CachingConfig extends CachingConfigurerSupport {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(final String name) {
                //cache for coin pairs, such as BTCUSD
                if (name.equals("CoinCache")) {
                    return new ConcurrentMapCache(name, CacheBuilder.newBuilder()
                            .expireAfterWrite(61, TimeUnit.SECONDS)
                            .maximumSize(1000)
                            .build()
                            .asMap(),
                            false);
                }
                if (name.equals("IconCache")) {
                    return new ConcurrentMapCache(name, CacheBuilder.newBuilder()
                            .expireAfterWrite(5, TimeUnit.DAYS)
                            .maximumSize(1000)
                            .build()
                            .asMap(),
                            false);
                }
                //cache for list of all coins
                if (name.contains("All24HourTicker")) {
                    return new ConcurrentMapCache(name, CacheBuilder.newBuilder()
                            .expireAfterWrite(5, TimeUnit.MINUTES)
                            //the maximum size number is rather arbitrary - the time is really the important issue
                            .maximumSize(5)
                            .build()
                            .asMap(),
                            false);
                }
                //cache for exchange information
                if (name.contains("ExchangeInfo")) {
                    return new ConcurrentMapCache(name, CacheBuilder.newBuilder()
                            .expireAfterWrite(1, TimeUnit.DAYS)
                            .maximumSize(5)
                            .build()
                            .asMap(),
                            false);
                }
                return null;
            }
        };
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}