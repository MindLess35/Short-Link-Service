package com.shortlink.webapp.config;

import com.shortlink.webapp.property.CacheProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.stream.Stream;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final CacheProperty cacheProperty;


    //    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
//        lettuceConnectionFactory.setHostName("localhost");
//        lettuceConnectionFactory.setPort(6379);
//        lettuceConnectionFactory.afterPropertiesSet();
//        return lettuceConnectionFactory;
//    }
    @Bean
    public <V extends Serializable> RedisTemplate<String, V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String, RedirectLinkDto> redisTemplateForLink(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, RedirectLinkDto> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }


    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(cacheProperty.getUserCacheName(),
                        RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(Duration.ofSeconds(cacheProperty.getUserCacheTtlInSeconds())))

                .withCacheConfiguration(cacheProperty.getImageCacheName(),
                        RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(Duration.ofSeconds(cacheProperty.getImageCacheTtlInSeconds())));
    }

    @Bean
    public CacheResolver imageCacheResolver(CacheManager cacheManager) {
        return context -> Stream.of(cacheProperty.getImageCacheName())
                .map(cacheManager::getCache)
                .toList();
    }

    @Bean
    public CacheResolver userCacheResolver(CacheManager cacheManager) {
        return context -> Stream.of(cacheProperty.getUserCacheName())
                .map(cacheManager::getCache)
                .toList();
    }

//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(60))
//                .disableCachingNullValues()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
//
//    @Bean
//    public RedisCacheConfiguration cache1Configuration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(5));
//    }
//
//    @Bean
//    public RedisCacheConfiguration cache2Configuration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofHours(1));
//    }
//
//    @Bean
//    public CacheManager cacheManager1(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cache1Configuration())
//                .build();
//    }

//    @Bean
//    public CacheManager cacheManager2(RedisConnectionFactory redisConnectionFactory) {
//        //new RedisCacheManager().getCache().evict();
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cache2Configuration())
//                .build();
//    }
//    @Cacheable(value = "cache1", cacheManager = "cacheManager1")
//    public String myCachedMethod1() {
//        // Ваш код
//        return "Cached Data 1";
//    }
//
//    @Cacheable(value = "cache2", cacheManager = "cacheManager2")
//    public String myCachedMethod2() {
//        // Ваш код
//        return "Cached Data 2";
//    }


}
